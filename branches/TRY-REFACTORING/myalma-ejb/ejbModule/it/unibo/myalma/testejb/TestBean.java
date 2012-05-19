package it.unibo.myalma.testejb;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.User;
import java.util.List;
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

import org.hornetq.jms.client.HornetQConnectionFactory;
import org.jboss.ejb3.annotation.Depends;
import org.jboss.logging.Logger;
import org.jboss.security.SecurityContextAssociation;


/**
 * Session Bean implementation class TestBean
 */
@Stateless
// Questa annotazione sembra essere ignorata da jboss 6 e 6.1 quidni utilizzare file EAR/META-INF/jboss-app.xml
// @SecurityDomain("myalma-security-domain")
//@Depends({"org.hornetq:module=JMS,name=\"java:/JmsXA\",type=ConnectionFactory"})
public class TestBean implements TestBeanRemote {
	
	private static final Logger log = Logger.getLogger(TestBean.class.getName());

	@PersistenceContext(unitName="myalma-jpa")
	EntityManager entityManager;
	
	@Resource
	SessionContext context;
	
	@Resource(mappedName="java:/JmsXA") 
	private TopicConnectionFactory topicConnectionFactory;
	
	@Resource(mappedName="/jms/topics/contentEvents") 
	private Topic eventsTopic;

	
    /**
     * Default constructor. 
     */
    public TestBean() {
        
    }

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Content> getAllContents() 
	{
//		log.info("Called getAllContents()");
//		Connection conn = null;
//			HornetQConnectionFactory fact = (HornetQConnectionFactory)topicConnectionFactory;
//			fact.setBlockOnDurableSend(true);
//			fact.setBlockOnAcknowledge(true);
//			fact.setBlockOnNonDurableSend(true);
		
			log.info(context.getCallerPrincipal().getName());

			Connection connection = null;
		    Session session = null;
		    MessageProducer sender = null;
		    try {
		    	// Non serve fare start sulla connessione
		    	connection = topicConnectionFactory.createConnection("alessandro.montanar5@studio.unibo.it","ale");
		    	// I due parametri sono ignorati dal container (vedi libro EJB 3.1)
		    	session = connection.createSession(true, 0);
		    	sender = session.createProducer(eventsTopic);
		    	TextMessage message = session.createTextMessage();
		    	message.setText("hello world");
		    	sender.send(message);
		    }
		    catch (Exception e) 
		    {
			
			}
		    finally {
		      if (sender != null) try { sender.close(); } catch (Exception ignore) { }
		      if (session != null) try { session.close(); } catch (Exception ignore) { }
		      if (connection != null) try { connection.close(); } catch (Exception ignore) { }
		    }
		
		
		
		// Usare sempre il nome della classe nelle query e non il nome della tabella in cui è mappata la classe
		return entityManager.createQuery("SELECT c FROM Content c").getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public void modifyTheContent(Content theContent) 
	{
		User modifier = entityManager.find(User.class, context.getCallerPrincipal().getName());
		theContent.setModifier(modifier);
		entityManager.merge(theContent);
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public void addContent(Content aContent) 
	{
		entityManager.persist(aContent);
	}

	@Override
	@RolesAllowed("admin")
	public User addUser(User aUser) 
	{
		entityManager.persist(aUser);
		return aUser;
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> getAllUsers() 
	{
		return entityManager.createQuery("SELECT u FROM User u").getResultList();
	}

	@Override
	@RolesAllowed("admin")
	public Role addRole(Role role) 
	{
		entityManager.persist(role);
		return role;
	}

	@Override
	public void removerRole(Role role) 
	{
		entityManager.remove(entityManager.find(Role.class, role.getId()));
		
	}

	@Override
	public void removeUser(User user) 
	{
		entityManager.remove(entityManager.find(User.class, user.getMail()));
		
	}


}
