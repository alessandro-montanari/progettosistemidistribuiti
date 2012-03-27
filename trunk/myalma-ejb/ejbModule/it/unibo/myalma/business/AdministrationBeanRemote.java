package it.unibo.myalma.business;
import java.util.List;
import java.util.Set;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import javax.ejb.Remote;

@Remote
public interface AdministrationBeanRemote 
{
	// Da rivedere------
	User addUser(User aUser);
	void removeUser(User user);
	
	Role addRole(Role role);
	void removerRole(Role role);
	
	Teaching addTeaching(Teaching teaching);
	Teaching addTeaching(Teaching teaching, String editorId);
	Teaching addTeaching(Teaching teaching, String editorId, String[] assistantIds);
	void removeTeaching(Teaching teaching);
	void removeTeachingById(int teachingId);
	
	void removeNotificationById(int notificationId);
}
