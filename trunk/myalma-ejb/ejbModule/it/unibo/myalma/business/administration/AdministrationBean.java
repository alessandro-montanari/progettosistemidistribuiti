package it.unibo.myalma.business.administration;

import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class AdministrationBean
 */
@Stateless
@Local(IAdministration.class)
//@Remote(it.unibo.myalma.business.remote.IAdministrationBeanRemote.class)
@RolesAllowed("admin")
public class AdministrationBean implements IAdministration 
{
	@PersistenceContext(unitName="myalma-jpa")
	EntityManager entityManager;

	@Override
	public User addUser(User aUser) 
	{
		entityManager.persist(aUser);
		return aUser;
	}

	@Override
	public Role addRole(Role role) 
	{
		entityManager.persist(role);
		return role;
	}

	@Override
	public void removeRole(Role role) 
	{
		entityManager.remove(entityManager.find(Role.class, role.getId()));
	}

	@Override
	public void removeUser(User user) 
	{
		entityManager.remove(entityManager.find(User.class, user.getMail()));
	}

	@Override
	public void removeTeaching(Teaching teaching) 
	{
		entityManager.remove(entityManager.find(Teaching.class, teaching.getId()));
	}

	@Override
	public Teaching addTeaching(Teaching teaching) 
	{
		// Qui assumiamo che il cliente abbia costruito un oggetto teaching completo e lo controlliamo
		if(teaching.getContentsRoot() == null)
			throw new IllegalArgumentException("It is not possible to add a teaching without a ContentsRoot");
		if(teaching.getContentsRoot().getEditor() == null)
			throw new IllegalArgumentException("It is not possible to add a teaching without editor");
		
		entityManager.persist(teaching);
		return teaching;
	}
	
	@Override
	public Teaching addTeaching(Teaching teaching, User editor) 
	{
		return addTeaching(teaching, editor, null);
	}

	@Override
	public Teaching addTeaching(Teaching teaching, User editor, Set<User> assistants) 
	{
		ContentsRoot root = new ContentsRoot(teaching.getName()+" Contents Root", editor);
		root.setEditor(editor);

		if(assistants != null)
		{
			// Attenzione: dato che il cascade sugli autori non c'è in ContentsRoot, gli autori devono essere già salvati sul DB
			root.setAuthors(assistants);
		}

		teaching.setContentsRoot(root);
		// Sfrutto il cascade sul ContestRoot in Teaching, quindi basta fare il PERSIST su Teaching per ottenere il PERSIST anche 
		// sul ContentsRoot associato
		entityManager.persist(teaching);
		return teaching;
	}
}
