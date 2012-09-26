package it.unibo.myalma.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.test.helpers.*;

// Decommentare il modulo di login per il testing in standalone-full.xml

// Prima classe testata
public class SearchBeanTestCase 
{
	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		searchBean = (ISearch)helper.lookup("ejb:myalma-ear/myalma-ejb//SearchBean!it.unibo.myalma.business.remote.ISearchRemote");
		automator.importData("./sql-scripts/myalma-dump.sql");
		helper.login("silvano.martello@uunibo.it", "silvano");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		helper.logout();
	}


	@Test
	public void testGetAllProfessors() 
	{
		assertEquals(4, searchBean.getAllProfessors().size());
	}

	@Test
	public void testFindProfessorsByName() 
	{
		assertEquals(1, searchBean.findProfessorsByName("Enrico").size());
		assertEquals("Enrico",searchBean.findProfessorsByName("Enrico").get(0).getName());
	}

	@Test
	public void testGetAllTeachings() 
	{
		assertEquals(8, searchBean.getAllTeachings().size());
	}

	@Test
	public void testFindTeachingByName() 
	{
		assertEquals("Statistica", searchBean.findTeachingByName("Statistica").getName());
	}

	@Test
	public void testFindTeachingsByYear() 
	{
		assertEquals(5, searchBean.findTeachingsByYear(1).size());
	}

	@Test
	public void testFindTeachingsByYearRange() 
	{
		assertEquals(2, searchBean.findTeachingsByYearRange(2,4).size());
		assertEquals(8, searchBean.findTeachingsByYearRange(1,10).size());
		assertEquals(1, searchBean.findTeachingsByYearRange(7,10).size());
	}

	@Test
	public void testFindTeachingsBySsd() 
	{
		assertEquals(7, searchBean.findTeachingsBySsd("MAT").size());
		assertEquals("Scienza delle merendine", searchBean.findTeachingsBySsd("INF").get(0).getName());
	}

	@Test
	public void testGetAllStudents() 
	{
		assertEquals(6, searchBean.getAllStudents().size());
	}

	@Test
	public void testGetAllContents() 
	{
		assertEquals(236, searchBean.getAllContents().size());
	}

	@Test
	public void testGetAllUsers() 
	{
		assertEquals(10, searchBean.getAllUsers().size());
	}

	@Test
	public void testFindAllAssistantsByTeachingInt() 
	{
		assertEquals(2,searchBean.findAssistantsByTeaching(137).size());
		assertEquals("paolo.bellavista@uunibo.it", searchBean.findAssistantsByTeaching(137).get(0).getMail());
	}

	@Test
	public void testFindAllAssistantsByTeachingString() 
	{
		assertEquals(2,searchBean.findAssistantsByTeaching("Ricerca Operativa").size());
		assertEquals("paolo.bellavista@uunibo.it", searchBean.findAssistantsByTeaching("Ricerca Operativa").get(0).getMail());
	}

	@Test
	public void testFindAllStudentsSubscribedToTeachingInt() 
	{
		assertEquals(2,searchBean.findStudentsSubscribedToTeaching(137).size());
		assertEquals(0, searchBean.findStudentsSubscribedToTeaching(131).size());
	}

	@Test
	public void testFindAllStudentsSubscribedToTeachingString() 
	{
		assertEquals(2,searchBean.findStudentsSubscribedToTeaching("Ricerca Operativa").size());
		assertEquals(0, searchBean.findStudentsSubscribedToTeaching("Scienza delle merendine").size());
		assertEquals(0, searchBean.findStudentsSubscribedToTeaching("").size());
	}

	@Test
	public void testFindAllTeachingsByEditorId() 
	{
		assertEquals(4, searchBean.findTeachingsByEditorId("silvano.martello@uunibo.it").size());
		assertEquals(4, searchBean.findTeachingsByEditorId("enrico.denti@uunibo.it").size());
	}

	@Test
	public void testFindAllTeachingsByEditorName() 
	{
		assertEquals(4, searchBean.findTeachingsByEditorName("Silvano").size());
		assertEquals(4, searchBean.findTeachingsByEditorName("Enrico").size());
	}

	@Test
	public void testFindAllTeachingsByAssistantId() 
	{
		assertEquals(1, searchBean.findTeachingsByAssistantId("paolo.bellavista@uunibo.it").size());
		assertEquals("Ricerca Operativa", searchBean.findTeachingsByAssistantId("paolo.bellavista@uunibo.it").get(0).getName());
		assertEquals(1, searchBean.findTeachingsByAssistantId("giuseppe.bellavia@uunibo.it").size());
	}

	@Test
	public void testFindAllTeachingsByAssistantName() 
	{
		assertEquals(1, searchBean.findTeachingsByAssistantName("Paolo").size());
		assertEquals("Ricerca Operativa", searchBean.findTeachingsByAssistantName("Paolo").get(0).getName());
		assertEquals(1, searchBean.findTeachingsByAssistantName("Giuseppe").size());
	}

	@Test
	public void testFindTeachingByContentsRootInt() 
	{
		assertEquals("Linguaggi", searchBean.findTeachingByContentsRoot(3330).getName());
		assertEquals("Statistica", searchBean.findTeachingByContentsRoot(3325).getName());
	}

	@Test
	public void testFindTeachingByContentsRootString() 
	{
		assertEquals("Linguaggi", searchBean.findTeachingByContentsRoot("Linguaggi Contents Root").getName());
		assertEquals("Statistica", searchBean.findTeachingByContentsRoot("Statistica Contents Root").getName());
	}

	@Test
	public void testFindContentsByTitle() 
	{
		assertEquals(ContentType.CONTENTS_ROOT, searchBean.findContentsByTitle("Fon. Informatica Contents Root").get(0).getContentType());
		assertEquals(ContentType.CATEGORY, searchBean.findContentsByTitle("Categoria1 Linguaggi").get(0).getContentType());
		assertEquals(1, searchBean.findContentsByTitle("Categoria1 Linguaggi").size());
	}

	@Test
	public void testFindContensByType() 
	{
		assertEquals(8, searchBean.findContentsByType(ContentType.CONTENTS_ROOT).size());
		assertEquals(115, searchBean.findContentsByType(ContentType.CATEGORY).size());
	}

	@Test
	public void testFindContentById() 
	{
		assertEquals("Scienza delle merendine Contents Root", searchBean.findContentById(3323).getTitle());
		assertEquals("Categoria2 Ricerca Operativa", searchBean.findContentById(3331).getTitle());
	}

	@Test
	public void testGetAllRoles() 
	{
		assertEquals(3, searchBean.getAllRoles().size());
	}

	@Test
	public void testGetAllNotifications() 
	{
		assertEquals(0, searchBean.getAllNotifications().size());
	}

	@Test
	public void testFindTeachingById() 
	{
		assertEquals("Scienza delle costruzioni", searchBean.findTeachingById(132).getName());
		assertEquals("Ingegneria vs Matematica", searchBean.findTeachingById(135).getName());
	}
	
	@Test
	public void testFindSubscriptionsByUserId() 
	{
		assertEquals(1, searchBean.findSubscriptionsByUserId("alessandro.montanar5@studio.unibo.it").size());
		assertEquals("Ricerca Operativa", searchBean.findSubscriptionsByUserId("alessandro.montanar5@studio.unibo.it").get(0).getTeaching().getName());
	}
	
	@Test
	public void testFindSubscriptionsToTeachingString() 
	{
		assertEquals(2, searchBean.findSubscriptionsToTeaching("Ricerca Operativa").size());
		assertEquals(0, searchBean.findSubscriptionsToTeaching("Scienza delle merendine").size());
	}
	
	@Test
	public void testFindSubscriptionsToTeachingInt() 
	{
		assertEquals(2, searchBean.findSubscriptionsToTeaching(137).size());
		assertEquals(0, searchBean.findSubscriptionsToTeaching(131).size());
	}

}
