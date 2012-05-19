package it.unibo.myalma.testejb;

import java.util.List;

import javax.ejb.Remote;

import it.unibo.myalma.model.*;

@Remote
public interface TestBeanRemote 
{
	
	public List<Content> getAllContents();
	public List<User> getAllUsers();
	
	public void modifyTheContent(Content theContent);
	public void addContent(Content aContent);
	
	public User addUser(User aUser);
	public Role addRole(Role role);
	
	void removerRole(Role role);
	void removeUser(User user);

}
