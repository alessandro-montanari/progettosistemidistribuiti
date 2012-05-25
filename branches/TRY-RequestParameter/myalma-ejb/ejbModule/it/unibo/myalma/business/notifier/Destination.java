package it.unibo.myalma.business.notifier;

public class Destination 
{
	private String destination = "";
	
	public Destination()
	{}
	
	public Destination(String destination)
	{
		this.setDestination(destination);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
