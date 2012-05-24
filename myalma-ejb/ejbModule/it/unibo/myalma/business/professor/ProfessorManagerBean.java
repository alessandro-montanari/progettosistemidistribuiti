package it.unibo.myalma.business.professor;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.Material;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.TypeOfChange;
import it.unibo.myalma.model.User;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;
import org.jboss.seam.annotations.Name;

import java.lang.reflect.Method;

import javax.ejb.Local;

import it.unibo.myalma.business.exceptions.*;

/**
 * Session Bean implementation class ProfessorManagerBean
 */
@Stateless
@Name("professorManager")
@Local(IProfessorManager.class)
@Remote(it.unibo.myalma.business.remote.IProfessorManagerRemote.class)
@RolesAllowed({"professor", "admin"})
public class ProfessorManagerBean implements IProfessorManager 
{
	private static final Logger log = Logger.getLogger(ProfessorManagerBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	@Resource(mappedName="java:/JmsXA") 
	private TopicConnectionFactory topicConnectionFactory;

	@Resource(mappedName="/jms/topics/contentEvents") 
	private Topic eventsTopic;
	
	public ProfessorManagerBean()
	{
	}

	private void sendMessage(String message)
	{
		Connection connection = null;
		Session session = null;
		MessageProducer sender = null;
		try {
			// Non serve fare start sulla connessione
			connection = topicConnectionFactory.createConnection("alessandro.montanar5@studio.unibo.it","ale");
			// I due parametri sono ignorati dal container (vedi libro EJB 3.1)
			session = connection.createSession(true, 0);
			sender = session.createProducer(eventsTopic);
			TextMessage msg = session.createTextMessage();
			msg.setText(message);
			sender.send(msg);
		}
		catch (Exception e) 
		{
			log.warn("Impossible to send JMS message, trace:" + e.getStackTrace());
		}
		finally {
			if (sender != null) try { sender.close(); } catch (Exception ignore) { }
			if (session != null) try { session.close(); } catch (Exception ignore) { }
			if (connection != null) try { connection.close(); } catch (Exception ignore) { }
		}
	}

	private String createMessage(Content content, TypeOfChange change)
	{
		String msg = "";
		if(change.equals(TypeOfChange.REMOVE))		// Messaggio opportunamente formattato in caso di rimozione
			msg =  content.getTitle() + 
			"|" + content.getDescription() + 
			"|" + content.getParentContent().getId();
		else
			msg = content.getId()+"";

		return change.toString()+"_"+msg;
	}

	private boolean checkTeachingPermission(Content content, User prof)
	{
		ContentsRoot root = (ContentsRoot) (content.getRoot() != null ? content.getRoot() : content);

		if(root.getEditor().equals(prof))
			return true;

		if(root.getAuthors().contains(prof))
			return true;

		return false;
	}

	@Override
	public Content appendContent(Content parent, Content content) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Content parentDB = entityManager.find(Content.class, parent.getId());

		if(parentDB == null)
			throw new IllegalArgumentException("The parent content provided is not valid");
		if(!checkTeachingPermission(parentDB, prof))
			throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");

		if(content.getCreator() == null || !(content.getCreator().equals(prof)))
			content.setCreator(prof);

		content.setModifier(prof);

		if(content.getId() == -1)
		{	
			// Nuovo contenuto quindi persist
			entityManager.persist(content);		// Faccio il persist (non sarebbe necessario dato i cascade) perch� cos� viene
												// valorizzato l'Id di content e posso e utilizzarlo
												// nella creazione del messaggio JMS
		}
		else // Contenuto spostato quindi merge prima di appenderlo, altrimenti errore al flush(): persist on a detach entity!
		{
			content = entityManager.merge(content);
		}
		
		content = parentDB.appendContent(content);

		entityManager.flush();		// Cos� sono sicuro che le modifiche saranno riportate sul DB prima che il messaggio sia
		// effettivamente inviato (al termine della transazione) (http://www.coderanch.com/t/321201/EJB-JEE/java/Stateless-session-bean-CMT-sending)
		// In pratica sia le modifiche al DB che l'invio del messaggio sono effettuate al commit della transazine
		// corrente ma dato che non so in che ordine vengono eseguite (se prima messaggio di modifiche potrebbe essere
		// un problema) preferisco forzare le modifiche sul DB (TODO ATTENZIONE POTREBBE NON ESSERE VERO)

		sendMessage(createMessage(content, TypeOfChange.INSERT));

		return content;
	}

	@Override
	public Content removeContent(Content content) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Content contentDB = entityManager.find(Content.class, content.getId());

		if(contentDB == null)
			throw new IllegalArgumentException("The content provided is not valid");
		
		// Devo fare cos� (invece di parent = contentDB.getParentContent()) perch� alrimenti in checkTeachingPermission quando faccio
		// il cast a ContentsRoot mi da eccezione (impossibile fare il cast)
		Content parent = entityManager.find(Content.class, contentDB.getParentContent().getId());
		
		if(!checkTeachingPermission(parent, prof))
			throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");

		parent.setModifier(prof);

		// Non � un problema inviare il messaggio qui (prima della rimozione) perch� comunque il messaggio viene effettivamente inviato
		// solo al commit della transazione, altrimenti non viene inviato
		sendMessage(createMessage(contentDB, TypeOfChange.REMOVE));

		contentDB = parent.removeContent(contentDB);

		entityManager.flush(); 	// Come sopra mi assicuro di riportare le modifiche prima dell'invio del messaggio
		
		return contentDB;
	}
	
	@Override
	public Content removeContent(Content parent, String contentTitle) 
	{
		Content parentDB = entityManager.find(Content.class, parent.getId());
		
		if(parentDB == null)
			throw new IllegalArgumentException("The parent provided is not valid");
		
		for(Content content : parentDB.getChildContents())
		{
			if(content.getTitle().equals(contentTitle))
			return removeContent(content);
		}
		
		throw new IllegalArgumentException("Impossible to find the content with name " + contentTitle);
	}

	@Override
	public void removeAllChildContents(Content parent) 
	{
		Content parentDB = entityManager.find(Content.class, parent.getId());
		
		if(parentDB == null)
			throw new IllegalArgumentException("The parent provided is not valid");

		for (Content cont : parentDB.getChildContents().toArray(new Content[0])) 
		{
			removeContent(cont);
		}	
	}

	// Metodo di comodo: aggiorna solo le propriet� del contenuto ma ignora eventuali modifiche rigurdanti il parent e il root 
	// (per questi utilizzare i metodi append e remove del bean)
	// Utile quando si modificano diverse propriet� in un colpo solo
	@Override
	public Content updateContent(Content content) 
	{
		User prof = null;
		Content contentDB = null;

		prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		contentDB = entityManager.find(Content.class, content.getId());

		if(contentDB == null)
			throw new IllegalArgumentException("The content is not present in the DB, add it first");
		if(!checkTeachingPermission(contentDB, prof))
			throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");


		contentDB.setTitle(content.getTitle());
		contentDB.setDescription(content.getDescription());
		if(content instanceof Information)
		{
			Information infoDB = (Information)contentDB;
			Information info = (Information)content;
			infoDB.setBody(info.getBody());
		}
		else if(content instanceof Material)
		{
			Material materialDB = (Material)contentDB;
			Material material = (Material)content;
			materialDB.setPath(material.getPath());
		}
		
		contentDB.setModifier(prof);

		entityManager.flush();

		sendMessage(createMessage(content, TypeOfChange.CHANGE));

		return contentDB;
	}

	@Override
	public Content updateContent(int contentId, String whatToModify, String newValue)
	{
		if(newValue == null || newValue.equals(""))
			throw new IllegalArgumentException("New value can not be empty");

		User prof = null;
		Content content = null;

		try {
			prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
			content = entityManager.find(Content.class, contentId);

			if(!checkTeachingPermission(content, prof))
				throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");

			Class<? extends Content> contentClass = content.getClass();
			String methodName = "set" + whatToModify.substring(0, 1).toUpperCase() + whatToModify.substring(1);
			Method method;

			method = contentClass.getMethod(methodName, String.class);

			method.invoke(content, newValue);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The property " + whatToModify + " does not exists");
		}

		content.setModifier(prof);

		entityManager.flush();

		sendMessage(createMessage(content, TypeOfChange.CHANGE));

		return content;
	}

	@Override
	public User updatePersonalInfo(String whatToModify, String newValue) 
	{
		if(newValue == null || newValue.equals(""))
			throw new IllegalArgumentException("New value can not be empty");

		User prof = null;

		try {
			prof = entityManager.find(User.class, context.getCallerPrincipal().getName());

			Class<? extends User> profClass = prof.getClass();
			String methodName = "set" + whatToModify.substring(0, 1).toUpperCase() + whatToModify.substring(1);
			Method method;
			method = profClass.getMethod(methodName, String.class);
			method.invoke(prof, newValue);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The property " + whatToModify + " does not exists");
		}

		return prof;
	}
	
	
	/**
	 * I due metodi successivi usano Teaching e User solo per la loro chiave primaria per andare a prendere nel DB l'oggetto managed
	 * corrispondente, quindi invece di passare l'intero oggetto si potrebbe passare solo la chiave primaria (era cos� in una vecchia
	 * versione), per� � stato deciso di passare l'oggetto per avere un codice pi� leggibile quando vengono invocati tali metodi.
	 * Inoltre dato che il web tier e "locale" all'EJB tier non c'� problema di overhead di rete, nel caso in cui questo bean debba
	 * essere utilizzato da remoto si potrebbe pensare a modificarne l'interfaccia passando solo le chiavi primarie.
	 */
	@Override
	public void addAssistant(Teaching teaching, User assistant) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Teaching t = entityManager.find(Teaching.class, teaching.getId());

		if(t == null)
			throw new IllegalArgumentException("The teaching provided is not valid");
		if(!(t.getContentsRoot().getEditor().equals(prof)))
			throw new PermissionException("The user " + prof.getMail() + " is not editor of " + t.getName());

		User asisstantDB = entityManager.find(User.class, assistant.getMail());

		if(asisstantDB == null)
			throw new IllegalArgumentException("The user " + assistant.getMail() + " is not present in the system");

		t.getContentsRoot().getAuthors().add(asisstantDB);
	}

	@Override
	public void removeAssistant(Teaching teaching, User assistant) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Teaching t = entityManager.find(Teaching.class, teaching.getId());

		if(t == null)
			throw new IllegalArgumentException("The teaching provided is not valid");
		if(!(t.getContentsRoot().getEditor().equals(prof)))
			throw new PermissionException("The user " + prof.getMail() + " is not editor of " + t.getName());

		User asisstantDB = entityManager.find(User.class, assistant.getMail());

		if(asisstantDB == null)
			throw new IllegalArgumentException("The user " + assistant.getMail() + " is not present in the system");
		if(!(t.getContentsRoot().getAuthors().contains(asisstantDB)))
			throw new IllegalArgumentException("The user " + assistant.getMail() + " is not an assistant for " + t.getName());
	
		t.getContentsRoot().getAuthors().remove(asisstantDB);
	}
}
