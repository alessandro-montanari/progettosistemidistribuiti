package it.unibo.myalma.business.notifier;

import it.unibo.myalma.business.notifier.Destination;
import it.unibo.myalma.business.notifier.INotifier;
import it.unibo.myalma.business.notifier.NotifierFactory;
import it.unibo.myalma.model.TypeOfChange;
import it.unibo.myalma.business.search.ISearch;

import javax.annotation.security.RunAs;
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

@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", 
				propertyValue = "javax.jms.Topic"),

				@ActivationConfigProperty(
						propertyName = "destination", 
						propertyValue = "jms/topics/contentEvents"),

						@ActivationConfigProperty(
								propertyName="subscriptionDurability", 
								propertyValue="Durable"),

								// Le prossime proprietˆ sono necessarie per sottoscrizioni durabili
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
@RunAs("admin") // Serve per poter invocare i metodi del searchBean
public class ContentNotifierBean implements MessageListener 
{	
	private static final Logger log = Logger.getLogger(ContentNotifierBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private ISearch searchBean;

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) 
	{
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
			
			// Qui logica per capire quale notificatore utilizzare (in questo caso solo mail) -------------
			
			INotifier notifier = NotifierFactory.getInstance().createNotifier("mail");
			
			// -----------------
			
			Destination dest = new Destination(u.getMail());

//			TODO Decommenta per inviare le notifiche
//			notifier.notify(dest, msg);
		}
	}

}
