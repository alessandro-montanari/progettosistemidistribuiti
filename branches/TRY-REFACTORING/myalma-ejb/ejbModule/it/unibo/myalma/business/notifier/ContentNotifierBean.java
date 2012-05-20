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

import it.unibo.myalma.model.Content;
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

		String typeOfChange = stringMessage.substring(0, stringMessage.indexOf("_"));
		String msg = "";

		if(typeOfChange.equalsIgnoreCase("REMOVE"))
		{
			String titolo = "<no titolo>";
			String descrizione = "<no descrizione>";
			int parentId = -1;
				
			StringTokenizer tokenizer = new StringTokenizer(stringMessage,"|_");

			// Salto il typeOfChange
			tokenizer.nextToken();
			
			if(tokenizer.countTokens() == 3)
			{
				titolo = tokenizer.nextToken();
				descrizione = tokenizer.nextToken();
				parentId = Integer.parseInt(tokenizer.nextToken());
			}
			else
			{
				// Manca la descrizione
				titolo = tokenizer.nextToken();
				parentId = Integer.parseInt(tokenizer.nextToken());
			}
			
			Content parent = entityManager.find(Content.class, parentId);

			msg = 	"Contenuto: " + titolo + "\n" +
					"Descrizione: " + descrizione + "\n" +
					"è stato RIMOSSO \n" +
					"dalla categoria " + parent.getTitle();

			List<Subscription> subscriptions = getSubscriptions(parent);
			sendNotifications(subscriptions, msg, null, typeOfChange);
		}
		else
		{
			int contentId = Integer.parseInt(stringMessage.substring(stringMessage.indexOf("_")+1));

			Content content = entityManager.find(Content.class, contentId);

			msg = 	"Contenuto: " + content.getTitle() + "\n" +
					"è stato ";

			if(typeOfChange.equalsIgnoreCase("CHANGE"))
			{
				msg += "MODIFICATO \n";
			}
			else if(typeOfChange.equalsIgnoreCase("INSERT"))
			{
				msg += "INSERITO \n";
			}

			List<Subscription> subscriptions = getSubscriptions(content);
			sendNotifications(subscriptions, msg, content, typeOfChange);
		}

	}

	private void sendNotifications(List<Subscription> subscriptions, String msg, Content content, String typeOfChange) 
	{
		// La notifica deve essere condivisa
		Notification notification = content == null ? new Notification(msg, TypeOfChange.valueOf(typeOfChange)) 
													: new Notification(msg, content.getId(), TypeOfChange.valueOf(typeOfChange));
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

	private List<Subscription> getSubscriptions(Content content) 
	{
		int rootId = content.getRoot() == null ? content.getId() : content.getRoot().getId();
		Teaching teaching = searchBean.findTeachingByContentsRoot(rootId);

		return searchBean.findSubscriptionsToTeaching(teaching.getId());
	}

}
