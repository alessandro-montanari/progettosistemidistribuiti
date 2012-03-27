package it.unibo.myalma.business;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class AdministrationBean
 */
@Stateless
public class AdministrationBean implements AdministrationBeanRemote 
{
	@PersistenceContext(unitName="myalma-jpa")
	EntityManager entityManager;

	/**
	 * Default constructor. 
	 */
	public AdministrationBean() 
	{
		//TODO Costruttore bean
	}

	@Override
	@RolesAllowed("admin")
	public User addUser(User aUser) 
	{
		entityManager.persist(aUser);
		return aUser;
	}



	@Override
	@RolesAllowed("admin")
	public Role addRole(Role role) 
	{
		entityManager.persist(role);
		return role;
	}

	@Override
	@RolesAllowed("admin")
	public void removerRole(Role role) 
	{
		entityManager.remove(entityManager.find(Role.class, role.getId()));

	}

	@Override
	@RolesAllowed("admin")
	public void removeUser(User user) 
	{
		entityManager.remove(entityManager.find(User.class, user.getMail()));

	}

	@Override
	@RolesAllowed("admin")
	public void removeTeaching(Teaching teaching) 
	{
		entityManager.remove(teaching);
	}

	@Override
	@RolesAllowed("admin")
	public void removeTeachingById(int teachingId) 
	{
		Teaching teaching = entityManager.find(Teaching.class, teachingId);
		entityManager.remove(teaching);
		return;
	}

	@Override
	@RolesAllowed("admin")
	public void removeNotificationById(int notificationId) 
	{
		Notification not = entityManager.find(Notification.class, notificationId);
		entityManager.remove(not);
	}

	@Override
	@RolesAllowed("admin")
	public Teaching addTeaching(Teaching teaching) 
	{
		// Qui assumiamo che il cliente abbia costruito un oggetto teaching completo e controlliamo
		if(teaching.getContentsRoot().getEditor() == null)
			throw new IllegalArgumentException("It is not possible to add a teaching without editor");
		
		entityManager.persist(teaching);
		return teaching;
	}
	
	@Override
	public Teaching addTeaching(Teaching teaching, String editorId) 
	{
		return addTeaching(teaching, editorId, null);
	}

	@Override
	@RolesAllowed("admin")
	public Teaching addTeaching(Teaching teaching, String editorID, String[] assistantIds) 
	{
		// Non servono controlli sui campi di teaching dato che sono giˆ controllati dal DB

		User editor = entityManager.find(User.class, editorID);
		ContentsRoot root = new ContentsRoot(teaching.getName()+" Contents Root", editor);
		root.setEditor(editor);

		if(assistantIds != null)
		{
			Set<User> assistants = new HashSet<User>();
			for (String id : assistantIds) 
			{
				User assistant = entityManager.find(User.class, id);
				if(assistant != null)
					assistants.add(assistant);
			}
			
			root.setAuthors(assistants);
		}

		teaching.setContentsRoot(root);
		entityManager.persist(teaching);
		return teaching;
	}

}
