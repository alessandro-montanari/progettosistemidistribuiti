package it.unibo.myalma.business.notifier;

// Rappresenta una generica destinazione a cui inviare un messaggio, la stringa destination pu˜ avere diversi significati: numero di 
// cellulare, indirizzo mail, ...
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
