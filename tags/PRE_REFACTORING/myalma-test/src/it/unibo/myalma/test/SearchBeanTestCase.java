package it.unibo.myalma.test;

import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
import static org.junit.Assert.*;

import it.unibo.myalma.business.SearchBeanRemote;
import it.unibo.myalma.test.helpers.*;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.AppCallbackHandler;
import org.jboss.security.client.SecurityClient;
import org.jboss.security.client.SecurityClientFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SearchBeanTestCase {

	private static TestHelper helper = null;
	private static SearchBeanRemote search = null;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		helper = new TestHelper();
		search = (SearchBeanRemote)helper.lookup("myalma-ear/SearchBean/remote");
		helper.login("", "");
	}
	
	@AfterClass
	public static void tearDown()
	{
		helper.logout();
	}
	
	@Before
	public void setUp() throws Exception 
	{
		
	}

	
	@Test
	public void testGetAllProfessorsWithAdmin() 
	{
		assertEquals(4, search.getAllProfessors().size());
	}

	@Test
	public void testFindProfessorsByName() 
	{
		assertEquals(1, search.findProfessorsByName("Silvano").size());
		assertEquals("Silvano", search.findProfessorsByName("Silvano").get(0).getName());
	}

	@Test
	public void testGetAllTeachings() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindTeachingsByName() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindTeachingsByYear() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindTeachingsByYearRange() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindTeachingsBySsd() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindAllTeachingsOfProfessorEditor() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetAllStudents() 
	{
		assertEquals(5, search.getAllStudents().size());
	}

	@Test
	public void testFindAllStudentsSubscribedToTeaching() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindTeachingOfContentsRoot() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindAllTeachingsOfProfessorAssistant() {
		fail("Not yet implemented"); // TODO
	}
}

