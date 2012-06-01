package it.unibo.myalma.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import it.unibo.myalma.business.administration.IAdministration;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;
import it.unibo.myalma.test.helpers.AutomateImportMYSQL;
import it.unibo.myalma.test.helpers.TestHelper;

import org.junit.BeforeClass;
import org.junit.Test;

public class AdministrationBeanTestCase {

	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IAdministration adminBean = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		searchBean = (ISearch)helper.lookup("myalma-ear/SearchBean/remote");
		adminBean = (IAdministration)helper.lookup("myalma-ear/AdministrationBean/remote");
		automator.importData("./sql-scripts/myalma-dump.sql");
	}

	@Test
	public void testAdd_RemoveUser() 
	{
		int previousUsers = searchBean.getAllUsers().size();
		User user = new User("newUser@mail.it","password","New","User");
		user = adminBean.addUser(user);
		assertEquals(previousUsers+1, searchBean.getAllUsers().size());

		// Non possono aggiungere un utente con la stessa mail
		try
		{
			adminBean.addUser(user);
			fail();
		}
		catch(Exception e)
		{
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(javax.transaction.RollbackException.class, e.getCause().getClass());
		}

		User user2 = new User("newUser2@mail.it","password","New2","User2");
		// Non possono aggiungere un utente con la stessa password
		try
		{
			adminBean.addUser(user2);
			fail();
		}
		catch(Exception e)
		{
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(javax.transaction.RollbackException.class, e.getCause().getClass());
		}
		
		// Controllo cascade su roles in User: essendo solo REFRESH il ruolo presente nell'utente non deve essere inserito nel DB
		User user3 = new User("mail","password2","New2","User2");
		user3.getRoles().add(new Role("newRole"));
		int previousRoles = searchBean.getAllRoles().size();
		try
		{
			adminBean.addUser(user3);
			fail();
		}
		catch(Exception e)
		{
			assertEquals(javax.transaction.RollbackException.class, e.getCause().getClass());
		}
		assertEquals(previousRoles,searchBean.getAllRoles().size());
		
		adminBean.removeUser(user);
		assertEquals(previousUsers, searchBean.getAllUsers().size());
	}

	@Test
	public void testAdd_RemoveRole() 
	{
		int previousRoles = searchBean.getAllRoles().size();
		Role role = new Role("newRole");
		role = adminBean.addRole(role);
		assertEquals(previousRoles+1, searchBean.getAllRoles().size());
		
		// Non si può aggiungere due volte lo stesso Role
		try
		{
			adminBean.addRole(role);
			fail();
		}
		catch(Exception e)
		{
			assertEquals(javax.persistence.PersistenceException.class, e.getCause().getClass());
		}
		
		adminBean.removeRole(role);
		assertEquals(previousRoles, searchBean.getAllRoles().size());
	}

	@Test
	public void testRemoveTeaching() 
	{
		int previousTeachings = searchBean.getAllTeachings().size();
		Teaching teaching = searchBean.getAllTeachings().get(0);
		adminBean.removeTeaching(teaching);
		assertEquals(previousTeachings-1, searchBean.getAllTeachings().size());
	}

	@Test
	public void testAddTeachingTeaching() 
	{
		int previousTeachings = searchBean.getAllTeachings().size();
		int previousContents = searchBean.getAllContents().size();
		ContentsRoot contentsRoot = new ContentsRoot("Matematica Root",searchBean.getAllUsers().get(0));
		contentsRoot.setEditor(searchBean.getAllUsers().get(0));
		Teaching teaching = new Teaching(6, 1, "MAT", "Matematica", contentsRoot);
		adminBean.addTeaching(teaching);
		assertEquals(previousTeachings+1, searchBean.getAllTeachings().size());
		assertEquals(previousContents+1, searchBean.getAllContents().size());
		
		teaching = new Teaching(6, 1, "MAT", "Matematica2", null);
		try
		{
			adminBean.addTeaching(teaching);
		}
		catch(Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
		}
		
		// Qui l'editor è null
		contentsRoot = new ContentsRoot("Matematica Root",searchBean.getAllUsers().get(0));
		teaching = new Teaching(6, 1, "MAT", "Matematica3", contentsRoot);
		try
		{
			adminBean.addTeaching(teaching);
		}
		catch(Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
		}	
	}

	@Test
	public void testAddTeachingTeachingUser() 
	{
		int previousTeachings = searchBean.getAllTeachings().size();
		int previousContents = searchBean.getAllContents().size();
		User editor = searchBean.getAllProfessors().get(0);
		Teaching teaching = new Teaching(6, 1, "MAT", "WWW", null);
		adminBean.addTeaching(teaching, editor);
		assertEquals(previousTeachings+1, searchBean.getAllTeachings().size());
		assertEquals(previousContents+1, searchBean.getAllContents().size());
		
		teaching = searchBean.findTeachingByName("WWW");
		boolean trovato = false;
		for(Teaching teach : searchBean.findTeachingsByEditorId(editor.getMail()))
		{
			if(teach.getId() == teaching.getId())
			{
				trovato = true;
				break;
			}
		}
		assertEquals(true, trovato);
		assertEquals(0, searchBean.findAssistantsByTeaching("WWW").size());
	}

	@Test
	public void testAddTeachingTeachingUserSetOfUser() 
	{
		int previousTeachings = searchBean.getAllTeachings().size();
		int previousContents = searchBean.getAllContents().size();
		Set<User> assistants = new HashSet<User>();
		User editor = searchBean.getAllProfessors().get(0);
		// Aggiungo gli studenti come assistenti tanto non c'è nessun controllo, vedi nota in ContentsRoot.java
		for(User u : searchBean.getAllStudents())
			assistants.add(u);
		Teaching teaching = new Teaching(6, 1, "MAT", "YYY", null);
		adminBean.addTeaching(teaching, editor, assistants);
		assertEquals(previousTeachings+1, searchBean.getAllTeachings().size());
		assertEquals(previousContents+1, searchBean.getAllContents().size());
		
		int students = searchBean.getAllStudents().size();
		teaching = searchBean.findTeachingByName("YYY");
		boolean trovato = false;
		for(Teaching teach : searchBean.findTeachingsByEditorId(editor.getMail()))
		{
			if(teach.getId() == teaching.getId())
			{
				trovato = true;
				break;
			}
		}
		assertEquals(true, trovato);
		assertEquals(students, searchBean.findAssistantsByTeaching("YYY").size());
	}

}
