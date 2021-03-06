package it.unibo.myalma.test;

import static org.junit.Assert.*;

import javax.ejb.EJBException;

import it.unibo.myalma.business.professor.IProfessorManager;
import it.unibo.myalma.business.exceptions.PermissionException;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;
import it.unibo.myalma.test.helpers.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Note:
 * - il Lazy Inizialization rende un po' pi� difficile il testing da fuori il container (aka cliente remoto), tale probelma
 * 	pu� essere risolto utilzzando ad esempio Arquillian che permette di eseguire i test all'interno del container. A sua volta
 * 	Arquillian � p� complesso da utilizzare se non si usa Maven e in caso di login di utenti, quindi per questi motivi non � stato
 * 	utilizzato qui.
 */

//Decommentare il modulo di login per il testing in myalma-ear/META-INF/myalma-jboss-beans.xml

public class ProfessorManagerBeanTestCase {

	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IProfessorManager profManager = null;
	
	
	protected static final String teaching1Name = "Ricerca Operativa";
	protected static final String teaching2Name = "Scienza delle merendine";
	protected static final String teaching3Name = "Linguaggi";
	protected static final String assistant1Name = "Paolo";
	protected static final String assistant2Name = "Giuseppe";
	protected static final String assistant3Name = "Enrico";
	protected static final String category1Name = "Categoria1 "+teaching1Name;
	protected static final int category2Id = 3331;
	protected static final String material1Name = "Materiale3";
	protected static final String category1NameBis = "Categoria1 "+teaching3Name;
	protected static final String info1Name = "Informazione3";
	
	@BeforeClass
	public static void setUp() throws Exception 
	{
		searchBean = (ISearch)helper.lookup("myalma-ear/SearchBean/remote");
		profManager = (IProfessorManager)helper.lookup("myalma-ear/ProfessorManagerBean/remote");
		automator.importData("./sql-scripts/myalma-dump.sql");
	}

	@Test
	public void testAddAssistant() 
	{
		helper.login("silvano.martello@uunibo.it", "silvano");
		User assistant = searchBean.findProfessorsByName(assistant2Name).get(0);
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);
		
		int previousAssistants = searchBean.findAssistantsByTeaching(teaching1.getId()).size();
		int previousTeachingsForAssistant = searchBean.findTeachingsByAssistantName(assistant.getName()).size();

		// Controllo l'aggiunta DAL titolare
		profManager.addAssistant(teaching1, assistant);
		assertEquals(previousAssistants+1, searchBean.findAssistantsByTeaching(teaching1.getId()).size());
		assertEquals(previousTeachingsForAssistant+1, searchBean.findTeachingsByAssistantName(assistant.getName()).size());

		// Controllo l'aggiunta DI un utente che non esiste
		User notPersistentuser = new User("da", "sds", "adds", "dsd");
		try
		{
			profManager.addAssistant(teaching1, notPersistentuser);
			fail();
		}
		catch (Exception e) 
		{}
		helper.logout();

		// Controllo l'aggiunta DA un utente che non � titolare dell'insegnamento
		helper.login("paolo.bellavista@uunibo.it", "paolo");
		try
		{
			profManager.addAssistant(teaching1, assistant);
			fail();
		}
		catch (EJBException e) 
		{ 
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(PermissionException.class, e.getCause().getClass());
		}
		helper.logout();
	}

	@Test
	public void testRemoveAssistant()
	{
		helper.login("silvano.martello@uunibo.it", "silvano");
		
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);
		User assistant = searchBean.findProfessorsByName(assistant3Name).get(0);
		
		int previousTeachingsForAssistant = searchBean.findTeachingsByAssistantName(assistant.getName()).size();
		
		profManager.removeAssistant(teaching1, assistant);
		
		assertEquals(previousTeachingsForAssistant-1, searchBean.findTeachingsByAssistantName(assistant.getName()).size());
		
		helper.logout();
	}

	@Test
	public void testUpdatePersonalInfo() 
	{
		helper.login("alessandro.montanar5@studio.unibo.it", "ale");
		User u = searchBean.findProfessorsByName("Alessandro").get(0);
		assertEquals("Alessandro", u.getName());
		profManager.updatePersonalInfo("name", "Montanari");
		u = searchBean.findProfessorsByName("Montanari").get(0);
		assertEquals("Montanari", u.getName());
		profManager.updatePersonalInfo("name", "Alessandro");

		// Test su campo inesistente
		try
		{
			profManager.updatePersonalInfo("names", "Montanari");
		}
		catch (EJBException e) 
		{ 
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(IllegalArgumentException.class, e.getCause().getClass());
		}
		helper.logout();
	}
	
	@Test
	public void testMoveContent() 
	{
		helper.login("silvano.martello@uunibo.it", "silvano");
		
		Content child = searchBean.findContentById(3334);
		int previousContents = searchBean.findContentsByTitle(child.getTitle()).size();
		int previousId = child.getId();
		child = profManager.removeContent(child);
		
		assertEquals(previousContents-1, searchBean.findContentsByTitle(child.getTitle()).size());
		
		ContentsRoot newParent = (ContentsRoot)searchBean.findContentById(3329);
		child = profManager.appendContent(newParent, child);
		
		assertEquals(previousContents, searchBean.findContentsByTitle(child.getTitle()).size());
		// Se cambia l'id significa che � stato rimosso e poi reinserito
		assertTrue(previousId != child.getId());
		helper.logout();
	}

	@Test
	public void testAppendContent() 
	{
		// Controllo l'aggiunta dall'inizializzazione
		int previousContents = searchBean.getAllContents().size();
		int previousCategories = searchBean.findContentsByType(ContentType.CATEGORY).size();
		int previousNotices = searchBean.findContentsByType(ContentType.NOTICE).size();
		
		// Test del titolare
		helper.login("silvano.martello@uunibo.it", "silvano");
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);
		Category content = new Category("Categoria Nuova","",null);
		Content nuovaCat = profManager.appendContent(teaching1.getContentsRoot(), content);
		assertEquals(previousContents+1, searchBean.getAllContents().size());
		assertEquals(previousCategories+1, searchBean.findContentsByType(ContentType.CATEGORY).size());
		assertEquals(1, searchBean.findContentsByTitle("Categoria Nuova").size());
		helper.logout();

		// Test: aggiunta da un assistente
		helper.login("paolo.bellavista@uunibo.it", "paolo");
		Information notice = new Information(ContentType.NOTICE,"Notizia Nuova","Notizia","",null);
		profManager.appendContent(nuovaCat, notice);
		assertEquals(previousNotices+1, searchBean.findContentsByType(ContentType.NOTICE).size());
		assertEquals(1, searchBean.findContentsByTitle("Notizia Nuova").size());
		helper.logout();

		// Test: aggiunta da un docente che NON pu� farlo
		helper.login("enrico.denti@uunibo.it", "enrico");
		Content notice2 = new Information(ContentType.NOTICE,"Notizia2","Notizia","",null);
		try
		{
			profManager.appendContent(nuovaCat, notice2);
			fail();
		}
		catch (EJBException e) 
		{ 
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(PermissionException.class, e.getCause().getClass());
		}
		assertEquals(previousNotices+1, searchBean.findContentsByType(ContentType.NOTICE).size());
		helper.logout();
	}

	@Test
	public void testRemoveContent() 
	{
		// Test del titolare
		helper.login("silvano.martello@uunibo.it", "silvano");

		int previousMaterials = searchBean.findContentsByType(ContentType.MATERIAL).size();
		Content mat = searchBean.findContentsByTitle(material1Name).get(0);
		profManager.removeContent(mat);
		assertEquals(previousMaterials-1, searchBean.findContentsByType(ContentType.MATERIAL).size());
		assertEquals(0, searchBean.findContentsByTitle(material1Name).size());
		
		int previousInfos = searchBean.findContentsByType(ContentType.INFORMATION).size();
		mat = searchBean.findContentsByTitle(info1Name).get(0);
		Content parent = searchBean.findContentById(3331);
		profManager.removeContent(parent, mat.getTitle());
		assertEquals(previousInfos-1, searchBean.findContentsByType(ContentType.INFORMATION).size());
		assertEquals(0, searchBean.findContentsByTitle(mat.getTitle()).size());
		helper.logout();
	}

	@Test
	public void testRemoveAllContents() 
	{
		// Test del titolare
		helper.login("silvano.martello@uunibo.it", "silvano");
		
		int previousContents = searchBean.getAllContents().size();
		Teaching teaching = searchBean.findTeachingByName(teaching2Name);

		// Rimuovo tutto sotto il contentsRoot
		profManager.removeAllChildContents(teaching.getContentsRoot());
		// Rimangono solo i contentsRoot e i contenuti del secondo teaching
		assertEquals(previousContents-6, searchBean.getAllContents().size());
		
		helper.logout();
	}

	@Test
	public void testUpdateContent() 
	{
		// Test del titolare (utilizzo il secondo teaching perch� il primo � stato mutilato dai test :))
		helper.login("enrico.denti@uunibo.it", "enrico");
		User u = searchBean.findProfessorsByName(assistant3Name).get(0);
		Content cat1 = searchBean.findContentsByTitle(category1NameBis).get(0);
		profManager.updateContent(cat1.getId(), "description", "Descrizione Test YYY");
		cat1 = searchBean.findContentsByTitle(category1NameBis).get(0);
		assertEquals("Descrizione Test YYY", cat1.getDescription());
		assertEquals(u, cat1.getModifier());
		
		// Test sull'aggiornamento di diverse propriet�
		cat1.setTitle("Modificata YYY");
		cat1.setDescription("Modificata YYY");
		profManager.updateContent(cat1);
		cat1 = searchBean.findContentsByTitle("Modificata YYY").get(0);
		assertEquals("Modificata YYY", cat1.getTitle());
		assertEquals("Modificata YYY", cat1.getDescription());
		assertEquals(u, cat1.getModifier());

		helper.logout();

		// Testo il getModifier()
		helper.login("giuseppe.bellavia@uunibo.it", "giuseppe");
		User giuseppe = searchBean.findProfessorsByName(assistant2Name).get(0);
		profManager.updateContent(cat1.getId(), "description", "Descrizione Test Giuseppe");
		cat1 = searchBean.findContentsByTitle(cat1.getTitle()).get(0);
		assertEquals("Descrizione Test Giuseppe", cat1.getDescription());
		assertEquals(giuseppe, cat1.getModifier());
		helper.logout();
	}
}
