package it.unibo.myalma.business.notifier;

public class NotifierFactory 
{
	private String[] notifierNames = {"mail"};
	private static NotifierFactory instance = null;
	
	
	private NotifierFactory()
	{
	}
	
	public static NotifierFactory getInstance()
	{
		if(instance == null)
			instance = new NotifierFactory();
		
		return instance;
	}
	
	public INotifier createNotifier(String name) throws Exception
	{
		if(name.equals("mail"))
			return new MailNotifier();
		else if(name.equals("sms"))
			return new SmsNotifier();
		else
			throw new Exception("Invalid argument " + name);
	}

	public String[] getNotifierNames() {
		return notifierNames;
	}
}
