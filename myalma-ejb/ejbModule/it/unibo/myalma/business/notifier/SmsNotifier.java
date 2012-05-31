package it.unibo.myalma.business.notifier;

// Solo di esempio
public class SmsNotifier implements INotifier 
{
	protected SmsNotifier()
	{}

	@Override
	public String getName() 
	{
		return "sms";
	}

	@Override
	public void notify(Destination destination, String message) {
		// TODO Auto-generated method stub
		
	}

}
