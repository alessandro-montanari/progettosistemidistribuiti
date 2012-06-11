package it.unibo.myalma.business.administration;

import java.util.Set;

import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

public interface IAdministration 
{
	User addUser(User aUser);
	void removeUser(User aUser);
	
	Role addRole(Role aRole);
	void removeRole(Role aRole);
	
	Teaching addTeaching(Teaching teaching);
	Teaching addTeaching(Teaching teaching, User editor);
	Teaching addTeaching(Teaching teaching, User editor, Set<User> assistants);
	void removeTeaching(Teaching teaching);
}
