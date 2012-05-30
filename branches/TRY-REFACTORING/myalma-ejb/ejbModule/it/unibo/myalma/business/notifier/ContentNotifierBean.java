package it.unibo.myalma.business.notifier;

import it.unibo.myalma.business.notifier.Destination;
import it.unibo.myalma.business.notifier.INotifier;
import it.unibo.myalma.business.notifier.NotifierFactory;
import it.unibo.myalma.model.TypeOfChange;
import it.unibo.myalma.business.search.ISearch;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Subscription;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import java.util.List;
import java.util.StringTokenizer;

import org.jboss.logging.Logger;

/**
 * Message-Driven Bean implementation class for: ContentNotifierBean
 *
 */

// TODO Ricordarsi di fare cache (con un dizionario) ogni volta che si usa un INotifier, così non è necessario
// accedere sempre alla factory
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", 
				propertyValue = "javax.jms.Topic"),

				@ActivationConfigProperty(
						propertyName = "destination", 
						propertyValue = "contentEvents"),

						@ActivationConfigProperty(
								propertyName="subscriptionDurability", 
								propertyValue="Durable"),

								// Le prossime proprietà sono necessarie per sottoscrizioni durabili
								@ActivationConfigProperty(
										propertyName="clientId", 
										propertyValue="ContentNotifierBean"),

										@ActivationConfigProperty(
												propertyName="user", 
												propertyValue="alessandro.montanar5@studio.unibo.it"),

												@ActivationConfigProperty(
														propertyName="password", 
														propertyValue="ale")
		})
public class ContentNotifierBean implements MessageListener 
{	
	private static final Logger log = Logger.getLogger(ContentNotifierBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private INotifier mailNotifier;

	@EJB
	private ISearch searchBean;

	/**
	 * Default constructor. 
	 */
	public ContentNotifierBean() 
	{
		try 
		{
			mailNotifier = NotifierFactory.getInstance().createNotifier("mail");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) 
	{
		// Bisognerebbe fare qualcosa che se il contenuto è stato modificato l'ultima volta 1 secondo fa
		// non si inserisce una nuova notifica, oppure se la notifica c'è già non la si inserisce
		// è abbastanza toso, perché si potrebbe considerare il tipo di notifica, esempio: se c'è una notifica di modifica
		// e quel contenuto viene rimosso all'ora la notifica di modifica può essere eliminata, ecc...

		log.info("-----------RICEVUTO MESSAGGIO JMS---------------");

		String stringMessage = "";
		try 
		{
			stringMessage = ((TextMessage)message).getText();
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
			return;
		}

		log.info(stringMessage);

		String msg = "";

		String typeOfChange = "";
		String titolo = "";
		String descrizione = "";
		String parent = "";
		String modificatore = "";
		String contentsRootTitle = "";

		StringTokenizer tokenizer = new StringTokenizer(stringMessage,"|");

		typeOfChange = tokenizer.nextToken();
		titolo = tokenizer.nextToken();
		descrizione = tokenizer.nextToken();
		parent = tokenizer.nextToken();
		modificatore = tokenizer.nextToken();
		contentsRootTitle = tokenizer.nextToken();

		Teaching teaching = searchBean.findTeachingByContentsRoot(contentsRootTitle);

		msg = 	"Corso: " + teaching.getName() + "\n" +
				"Contenuto: " + titolo + "\n" +
				"Descrizione: " + descrizione + "\n" +
				"Categoria Padre: " + parent + "\n" +
				"Professore: " + modificatore + "\n" +
				"Evento: " + translateEvent(typeOfChange);

		List<Subscription> subscriptions = searchBean.findSubscriptionsToTeaching(teaching.getId());
		sendNotifications(subscriptions, msg, typeOfChange);
	}

	private String translateEvent(String typeOfChange) 
	{
		if(TypeOfChange.valueOf(typeOfChange) == TypeOfChange.CHANGE)
			return "MODIFICATO";
		else if(TypeOfChange.valueOf(typeOfChange) == TypeOfChange.INSERT)
			return "INSERITO";
		else
			return "RIMOSSO";
	}

	private void sendNotifications(List<Subscription> subscriptions, String msg, String typeOfChange) 
	{
		// La notifica deve essere condivisa
		Notification notification = new Notification(msg, TypeOfChange.valueOf(typeOfChange));
		entityManager.persist(notification);

		for(Subscription sub : subscriptions)
		{
			// Aggiungo la notifica al DB
			sub.getUnreadNotifications().add(notification);

			// Invio la mail
			User u = searchBean.findUserBySubscription(sub.getId());
			Destination dest = new Destination(u.getMail());

//			TODO Decommenta per inviare le mail
//			mailNotifier.notify(dest, msg);
		}
	}

}
