package it.unibo.myalma.test;

import static org.junit.Assert.*;

import javax.ejb.EJBException;

import it.unibo.myalma.business.AdministrationBeanRemote;
import it.unibo.myalma.business.IProfessorManager;
import it.unibo.myalma.business.PermissionException;
import it.unibo.myalma.business.SearchBeanRemote;
import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.Material;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;
import it.unibo.myalma.test.helpers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfessorManagerBeanTestCase extends TestsCommon {

	@BeforeClass
	public static void setUp() throws Exception 
	{
		init();
		fillDB();
	}

	@AfterClass
	public static void tearDown()
	{
		cleanDB();
	}

	@Test
	public void testAddAssistant() 
	{
		helper.login("silvano.martello@unibo.it", "silvano");
		// Controllo l'aggiunta dall'inizializzazione
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);
		assertEquals(1, searchBean.findAllAssistantsByTeaching(teaching1.getId()).size());

		// Controllo l'aggiunta DAL titolare
		User u = searchBean.findProfessorsByName(assistant2Name).get(0);
		profManager.addAssistant(teaching1.getId(), u.getMail());
		assertEquals(2, searchBean.findAllAssistantsByTeaching(teaching1.getId()).size());
		assertEquals(2, searchBean.findAllTeachingsByAssistantName(u.getName()).size());

		// Controllo l'aggiunta DI un utente che non esiste
		User notPersistentuser = new User("da", "sds", "adds", "dsd");
		try
		{
			profManager.addAssistant(teaching1.getId(), notPersistentuser.getMail());
			fail();
		}
		catch (Exception e) 
		{}
		helper.logout();

		// Controllo l'aggiunta DA un utente che non è titolare dell'insegnamento
		helper.login("paolo.bellavista@unibo.it", "paolo");
		try
		{
			profManager.addAssistant(teaching1.getId(), u.getMail());
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
		helper.login("silvano.martello@unibo.it", "silvano");
		
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);

		User u = searchBean.findProfessorsByName(assistant1Name).get(0);
		profManager.removeAssistant(teaching1.getId(), u.getMail());
		assertEquals(0, searchBean.findAllTeachingsByAssistantName(u.getName()).size());
		profManager.addAssistant(teaching1.getId(), u.getMail());
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
	public void testAppendContent() 
	{
		// Controllo l'aggiunta dall'inizializzazione
		assertEquals(218, searchBean.getAllContents().size());

		// Test del titolare
		helper.login("silvano.martello@unibo.it", "silvano");
		Teaching teaching1 = searchBean.findTeachingByName(teaching1Name);
		Category content = new Category("Categoria Nuova","",null);
		int nuovaCatId = profManager.appendContent(teaching1.getContentsRoot().getId(), content);
		assertEquals(12, searchBean.getAllContents().size());
		assertEquals(4, searchBean.findContensByType(ContentType.CATEGORY).size());
		assertEquals(1, searchBean.findContentsByTitle("Categoria Nuova").size());
		helper.logout();

		// Test: aggiunta da un assistente
		helper.login("paolo.bellavista@unibo.it", "paolo");
		Information notice = new Information(ContentType.NOTICE,"Notizia Nuova","Notizia","",null);
		profManager.appendContent(nuovaCatId, notice);
		assertEquals(1, searchBean.findContensByType(ContentType.NOTICE).size());
		assertEquals(1, searchBean.findContentsByTitle("Notizia Nuova").size());
		helper.logout();

		// Test: aggiunta da un docente che non può farlo
		helper.login("enrico.denti@unibo.it", "enrico");
		Content notice2 = new Information(ContentType.NOTICE,"Notizia2","Notizia","",null);
		try
		{
			profManager.appendContent(nuovaCatId, notice2);
			fail();
		}
		catch (EJBException e) 
		{ 
			// Controllo che sia lanciata la giusta eccezione
			assertEquals(PermissionException.class, e.getCause().getClass());
		}
		assertEquals(1, searchBean.findContensByType(ContentType.NOTICE).size());
		helper.logout();
	}

	@Test
	public void testRemoveContent() 
	{
		// Test del titolare
		helper.login("silvano.martello@unibo.it", "silvano");

		Content cat1 = searchBean.findContentsByTitle(category1Name).get(0);
		Content mat1 = searchBean.findContentsByTitle(material1Name).get(0);
		profManager.removeContent(cat1.getId(), mat1.getId());
		assertEquals(3, searchBean.findContensByType(ContentType.MATERIAL).size());
		assertEquals(0, searchBean.findContentsByTitle(material1Name).size());
		helper.logout();
	}

	@Test
	public void testRemoveAllContents() 
	{
		// Test del titolare
		helper.login("silvano.martello@unibo.it", "silvano");
		Teaching teaching = searchBean.findTeachingByName(teaching1Name);

		// Rimuovo tutto sotto il contentsRoot
		profManager.removeAllContents(teaching.getContentsRoot().getId());
		// Rimangono solo i contentsRoot e i contenuti del secondo teaching
		assertEquals(2+4, searchBean.getAllContents().size());
		assertEquals(1, searchBean.findContensByType(ContentType.CATEGORY).size());
		assertEquals(1, searchBean.findContensByType(ContentType.INFORMATION).size());
		assertEquals(0, searchBean.findContensByType(ContentType.NOTICE).size());
		assertEquals(0, searchBean.findContentsByTitle(category1Name).size());
		assertEquals(0, searchBean.findContentsByTitle(material2Name).size());
		helper.logout();
	}

	@Test
	public void testUpdateContent() 
	{
		// Test del titolare (utilizzo il secondo teaching perché il primo è stato mutilato dai test :))
		helper.login("enrico.denti@unibo.it", "enrico");
		User u = searchBean.findProfessorsByName(editor2Name).get(0);
		Content cat1 = searchBean.findContentsByTitle(category1NameBis).get(0);
		profManager.updateContent(cat1.getId(), "description", "Descrizione Test");
		cat1 = searchBean.findContentsByTitle(category1NameBis).get(0);
		assertEquals("Descrizione Test", cat1.getDescription());
		assertEquals(u, cat1.getModifier());
		
		// Test sull'aggiornamento di diverse proprietà
		Information info = (Information)searchBean.findContentsByTitle(info1NameBis).get(0);
		info.setTitle("Modificata");
		info.setDescription("Modificata");
		info.setBody("Modificata");
		profManager.updateContent(info);
		info = (Information)searchBean.findContentsByTitle("Modificata").get(0);
		assertEquals("Modificata", info.getTitle());
		assertEquals("Modificata", info.getDescription());
		assertEquals("Modificata", info.getBody());
		assertEquals(u, cat1.getModifier());

		helper.logout();

		// Testo il getModifier()
		helper.login("giuseppe.bellavia@unibo.it", "giuseppe");
		User giuseppe = searchBean.findProfessorsByName(assistant2Name).get(0);
		profManager.updateContent(cat1.getId(), "description", "Descrizione Test Giuseppe");
		cat1 = searchBean.findContentsByTitle(category1NameBis).get(0);
		assertEquals("Descrizione Test Giuseppe", cat1.getDescription());
		assertEquals(giuseppe, cat1.getModifier());
		helper.logout();

	}
}
