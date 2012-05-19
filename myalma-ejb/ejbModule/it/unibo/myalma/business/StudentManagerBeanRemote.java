package it.unibo.myalma.business;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import javax.ejb.Local;

@Local
public interface StudentManagerBeanRemote 
{
	void subscribeToTeaching(Teaching teaching);
	void desubscribeFromTeaching(Teaching teaching);
	
//	List<Notification> 

//	void change
}
