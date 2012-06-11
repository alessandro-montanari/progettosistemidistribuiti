package it.unibo.myalma.test;

import static org.junit.Assert.*;

import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.business.student.IStudentManager;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.test.helpers.AutomateImportMYSQL;
import it.unibo.myalma.test.helpers.TestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StudentManagerTestCase {

	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IStudentManager studentBean = null;

	@BeforeClass
	public static void setUp() throws Exception 
	{
		searchBean = (ISearch)helper.lookup("myalma-ear/SearchBean/remote");
		studentBean = (IStudentManager)helper.lookup("myalma-ear/StudentManagerBean/remote");
		automator.importData("./sql-scripts/myalma-dump.sql");
	}

	@Before
	public void setUpTest()
	{
		helper.login("alessandro.montanar5@studio.unibo.it", "alessandro");
	}

	@After
	public void tearDownTest()
	{
		helper.logout();
	}

	@Test
	public void testSubscribeToTeaching() 
	{
		Teaching teaching = searchBean.findTeachingByName("Linguaggi");
		int previousSubscruptions = searchBean.findSubscriptionsToTeaching(teaching.getId()).size();
		studentBean.subscribeToTeaching(teaching);
		assertEquals(previousSubscruptions+1, searchBean.findSubscriptionsToTeaching(teaching.getId()).size());

		// Test su sottoscrizione doppia
		try
		{
			studentBean.subscribeToTeaching(teaching);
			fail();
		}
		catch(Exception e)
		{
			assertEquals(IllegalStateException.class, e.getCause().getClass());
			assertEquals(previousSubscruptions+1, searchBean.findSubscriptionsToTeaching(teaching.getId()).size());
		}

		// Test di sottoscrizione da un docente
		helper.logout();
		helper.login("silvano.martello@uunibo.it", "silvano");
		try
		{
			studentBean.subscribeToTeaching(teaching);
			fail();
		}
		catch(Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals(previousSubscruptions+1, searchBean.findSubscriptionsToTeaching(teaching.getId()).size());
		}
	}

	@Test
	public void testDesubscribeFromTeaching() 
	{
		Teaching teaching = searchBean.findTeachingByName("Ricerca Operativa");
		int previousSubscruptions = searchBean.findSubscriptionsToTeaching(teaching.getId()).size();
		studentBean.desubscribeFromTeaching(teaching);
		assertEquals(previousSubscruptions-1, searchBean.findSubscriptionsToTeaching(teaching.getId()).size());

		// Test di desottoscrizione da un docente
		helper.logout();
		helper.login("silvano.martello@uunibo.it", "silvano");
		try
		{
			studentBean.desubscribeFromTeaching(teaching);
			fail();
		}
		catch(Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
			assertEquals(previousSubscruptions-1, searchBean.findSubscriptionsToTeaching(teaching.getId()).size());
		}
	}
}
