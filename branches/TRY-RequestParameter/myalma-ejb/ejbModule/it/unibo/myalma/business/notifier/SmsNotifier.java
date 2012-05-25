package it.unibo.myalma.business.notifier;


public class SmsNotifier implements INotifier 
{
	
	protected SmsNotifier()
	{
	}

//	@Override
//	public void notify(Destination destination, Content content, TypeOfChange type) {
//		// TODO Implementa notifica via sms
//
//	}

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
