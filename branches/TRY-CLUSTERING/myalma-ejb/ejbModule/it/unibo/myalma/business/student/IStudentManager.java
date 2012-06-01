package it.unibo.myalma.business.student;

import it.unibo.myalma.model.Teaching;

// Per ora molto semplice, un domani si dovranno aggiungere i metodi per la modifica delle policy di notifica e per l'aggiunta
// di altri tipi di notificatori
public interface IStudentManager 
{
	void subscribeToTeaching(Teaching teaching);
	void desubscribeFromTeaching(Teaching teaching);
}
