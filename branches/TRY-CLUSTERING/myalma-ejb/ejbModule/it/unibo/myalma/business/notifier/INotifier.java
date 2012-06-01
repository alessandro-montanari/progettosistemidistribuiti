package it.unibo.myalma.business.notifier;

// Notificatore in grado di inviare un messaggio testuale (stringa) ad una certa destinazione
public interface INotifier 
{
	void notify(Destination destination, String message);
	String getName();
}
