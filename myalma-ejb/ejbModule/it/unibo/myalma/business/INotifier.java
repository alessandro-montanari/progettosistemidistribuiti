package it.unibo.myalma.business;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.TypeOfChange;

public interface INotifier 
{
	// TODO Da rivedere la logica di utilizzo. Ad alto livello lui deve solo inviare stringhe (se voglio qualcosa di più elaborato utilizzo le sottoclassi)
	// il formato della stringa deve essere definito fuori
	
	
//	void notify(Destination destination, Content content, TypeOfChange type);
	void notify(Destination destination, String message);
	String getName();
}
