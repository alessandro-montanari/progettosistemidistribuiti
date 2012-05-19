package it.unibo.myalma.business;

import java.util.Date;
import java.util.Hashtable;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.mail.Session;


public class MailNotifier implements INotifier 
{
	private Session session;

	private Context ctx = null;

	protected MailNotifier()
	{
		Hashtable<String, String> prop = new Hashtable<String, String>();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.PROVIDER_URL, "jnp://localhost:1099/");
		try 
		{
			ctx = new InitialContext(prop);
			session = (Session) ctx.lookup("java:/Mail");
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
		}
	}

	private void sendMessage(Destination destination, String msg)
	{
		Message message = new MimeMessage(session);

		try
		{
			// Adjust the recipients. Here we have only one
			// recipient. The recipient's address must be
			// an object of the InternetAddress class.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination.getDestination(), false));

			// Set the message's subject
			message.setSubject("Notifica myAlma");

			// Insert the message's body
			message.setText(msg);

			// This is not mandatory, however, it is a good
			// practice to indicate the software which
			// constructed the message.
			message.setHeader("myAlma-Mailer", "myAlma Mailer");

			// Adjust the date of sending the message
			Date timeStamp = new Date();
			message.setSentDate(timeStamp);

			// Use the 'send' static method of the Transport
			// class to send the message
			Transport.send(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

//	@Override
//	public void notify(Destination destination, Content content, TypeOfChange type) 
//	{
//		String message;
//		message = 	"Contenuto: " + content.getTitle() + "\n" +
//					"Descrizione: " + content.getDescription() + "\n" +
//					"è stato ";
//		
//		if(type.equals(TypeOfChange.CHANGE))
//		{
//			message += "MODIFICATO \n";
//		}
//		else if(type.equals(TypeOfChange.INSERT))
//		{
//			message += "INSERITO \n";
//		}
//		
//		sendMessage(destination, message);
//	}

	@Override
	public void notify(Destination destination, String message) 
	{
		sendMessage(destination, message);
	}

	@Override
	public String getName() 
	{
		return "mail";
	}



}
