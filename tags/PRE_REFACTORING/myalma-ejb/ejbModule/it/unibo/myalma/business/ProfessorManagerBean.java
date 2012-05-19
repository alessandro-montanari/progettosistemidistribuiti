package it.unibo.myalma.business;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.Material;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.TypeOfChange;
import it.unibo.myalma.model.User;
import it.unibo.myalma.testejb.TestBean;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
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

/**
 * Session Bean implementation class ProfessorManagerBean
 */
@Stateless
@Name("professorManager")
@Local(IProfessorManager.class)
@RolesAllowed({"professor", "admin"})
public class ProfessorManagerBean implements IProfessorManager 
{
	private static final Logger log = Logger.getLogger(TestBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	@Resource(mappedName="java:/JmsXA") 
	private TopicConnectionFactory topicConnectionFactory;

	@Resource(mappedName="/jms/topics/contentEvents") 
	private Topic eventsTopic;

	/**
	 * Default constructor. 
	 */
	public ProfessorManagerBean() {
		// TODO Costruttore bean
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
	public void addAssistant(int teachingId, String assistantMail) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Teaching t = entityManager.find(Teaching.class, teachingId);

		if(!(t.getContentsRoot().getEditor().equals(prof)))
			throw new PermissionException("The user " + prof.getMail() + " is not editor of " + t.getName());

		User asisstant = entityManager.find(User.class, assistantMail);

		if(asisstant == null)
			throw new IllegalArgumentException("The user " + assistantMail + " is not present in the system");

		t.getContentsRoot().getAuthors().add(asisstant);
	}

	@Override
	public int appendContent(int parentId, Content content) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Content parent = entityManager.find(Content.class, parentId);

		if(!checkTeachingPermission(parent, prof))
			throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");

		if(content.getCreator() == null || !(content.getCreator().equals(prof)))
			content.setCreator(prof);

		content.setModifier(prof);
		
//		Content contentDB = entityManager.find(Content.class, content.getId());
		if(content.getId() == -1)
		{	
			// Nuovo contenuto quindi persist
			entityManager.persist(content);		// Faccio il persist (non sarebbe necessario dato i cascade e l'istruzione successiva) perché
												// così viene valorizzato l'Id di content e posso restituirlo al termine del metodo e utilizzarlo
												// nella creazione del messaggio JMS
		}
		
		// else: Contenuto probabilmente spostato quindi niente da fare
		
		parent.appendContent(content);		
		

		entityManager.flush();		// Così sono sicuro che le modifiche saranno riportate sul DB prima che il messaggio sia
		// effettivamente inviato (al termine della transazione) (http://www.coderanch.com/t/321201/EJB-JEE/java/Stateless-session-bean-CMT-sending)
		// In pratica sia le modifiche al DB che l'invio del messaggio sono effettuate al commit della transazine
		// corrente ma dato che non so in che ordine vengono eseguite (se prima messaggio di modifiche potrebbe essere
		// un problema) preferisco forzare le modifiche sul DB (TODO ATTENZIONE POTREBBE NON ESSERE VERO)

		sendMessage(createMessage(content, TypeOfChange.INSERT));

		return content.getId();
	}

	@Override
	public void removeContent(int parentId, int contentId) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Content parent = entityManager.find(Content.class, parentId);

		if(!checkTeachingPermission(parent, prof))
			throw new PermissionException("The user " + prof.getMail() + " has no permission for the operation");

		Content content = entityManager.find(Content.class, contentId);

		parent.setModifier(prof);

		// Non è un problema inviare il messaggio qui (prima della rimozione) perché comunque il messaggio viene effettivamente inviato
		// solo al commit della transazione, altrimenti non viene inviato
		sendMessage(createMessage(content, TypeOfChange.REMOVE));

		parent.removeContent(content);

		entityManager.flush(); 	// Come sopra mi assicuro di riportare le modifiche prima dell'invio del messaggio
	}

	@Override
	public void removeAllContents(int parentId) 
	{
		Content parent = entityManager.find(Content.class, parentId);

		for (Content cont : parent.getChildContents()) 
		{
			removeContent(parentId, cont.getId());
		}	
	}

	// Metodo di comodo: aggiorna solo le proprietà del contenuto ma ignora eventuali modifiche rigurdanti il parent e il root (per questi utilizzare
	// i metodi append e remove del bean)
	// Utile quando si modificano diverse proprietà in un colpo solo
	@Override
	public void updateContent(Content content) 
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

		return;
	}

	@Override
	public void updateContent(int contentId, String whatToModify, String newValue)
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

		return;
	}

	@Override
	public void updatePersonalInfo(String whatToModify, String newValue) 
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

		return;
	}

	@Override
	public void removeAssistant(int teachingId, String assistantMail) 
	{
		User prof = entityManager.find(User.class, context.getCallerPrincipal().getName());
		Teaching t = entityManager.find(Teaching.class, teachingId);

		if(!(t.getContentsRoot().getEditor().equals(prof)))
			throw new PermissionException("The user " + prof.getMail() + " is not editor of " + t.getName());

		User asisstant = entityManager.find(User.class, assistantMail);

		if(asisstant == null)
			throw new IllegalArgumentException("The user " + assistantMail + " is not present in the system");

		t.getContentsRoot().getAuthors().remove(asisstant);
	}
}
