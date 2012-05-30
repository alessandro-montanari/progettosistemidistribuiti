package it.unibo.myalma.test;

import static org.junit.Assert.*;

import it.unibo.myalma.business.professor.IEditContent;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.test.helpers.AutomateImportMYSQL;
import it.unibo.myalma.test.helpers.TestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Prima di avviare i test:
 * 	- commentare le annotazioni @In e @Out su content e parentContent e @RequestParameter su contentId
 * 	- sostituire @In su entityManager con @PersistenceContext(type=PersistenceContextType.EXTENDED) 
 */
public class EditContentBeanTestCase {
	
	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IEditContent editContentBean = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		searchBean = (ISearch)helper.lookup("myalma-ear/SearchBean/remote");
		automator.importData("./sql-scripts/myalma-dump.sql");
	}
	
	@Before
	public void setUpBeforeTest()
	{
		helper.login("silvano.martello@uunibo.it", "silvano");
		editContentBean = (IEditContent)helper.lookup("myalma-ear/EditContentBean/remote");
		editContentBean.init();
	}
	
	@After
	public void tearDownTest()
	{
		editContentBean.destroy();
		helper.logout();
	}

	@Test
	public void testCreateNewContent() 
	{
		editContentBean.newContent(ContentType.CATEGORY);
		assertEquals(ContentType.CATEGORY, editContentBean.getContent().getContentType());
		editContentBean.newContent(ContentType.INFORMATION);
		assertEquals(ContentType.INFORMATION, editContentBean.getContent().getContentType());
		editContentBean.newContent(ContentType.MATERIAL);
		assertEquals(ContentType.MATERIAL, editContentBean.getContent().getContentType());
		editContentBean.newContent(ContentType.NOTICE);
		assertEquals(ContentType.NOTICE, editContentBean.getContent().getContentType());
	}
	
	@Test
	public void testModifyNewContent() 
	{
		int previousInformations = searchBean.findContentsByType(ContentType.INFORMATION).size();
		editContentBean.newContent(ContentType.INFORMATION);
		editContentBean.setParentContentId(searchBean.findContentsByType(ContentType.CATEGORY).get(0).getId());
		editContentBean.save();
		assertEquals(previousInformations+1, searchBean.findContentsByType(ContentType.INFORMATION).size());	
		
		previousInformations = searchBean.findContentsByType(ContentType.INFORMATION).size();
		editContentBean.newContent(ContentType.INFORMATION);
		editContentBean.setParentContentId(searchBean.findContentsByType(ContentType.CATEGORY).get(0).getId());
		editContentBean.cancel();
		assertEquals(previousInformations, searchBean.findContentsByType(ContentType.INFORMATION).size());
	}
	
	private void setUpAdHoc()
	{
		editContentBean.destroy();
		helper.logout();
		helper.login("giuseppe.bellavia@uunibo.it", "giuseppe");
		editContentBean = (IEditContent)helper.lookup("myalma-ear/EditContentBean/remote");
		editContentBean.init();
	}
	
	@Test
	public void testModifyExistingContent()
	{
		setUpAdHoc();
		Content content = searchBean.findContentsByTitle("Categoria2 Linguaggi").get(0);
		assertEquals("Enrico", content.getModifier().getName());
		editContentBean.setContentId(content.getId());
		editContentBean.edit();
		editContentBean.cancel();
		content = searchBean.findContentsByTitle("Categoria2 Linguaggi").get(0);
		assertEquals("Enrico", content.getModifier().getName());
		
		editContentBean.edit();
		editContentBean.save();
		content = searchBean.findContentsByTitle("Categoria2 Linguaggi").get(0);
		assertEquals("Giuseppe", content.getModifier().getName());
	}

	@Test
	public void testDelete() 
	{
		int previousCategories = searchBean.findContentsByType(ContentType.CATEGORY).size();
		Content content = searchBean.findContentsByTitle("Categoria3 Ricerca Operativa").get(0);
		editContentBean.setContentId(content.getId());
		editContentBean.edit();
		editContentBean.delete();
		assertEquals(previousCategories-1, searchBean.findContentsByType(ContentType.CATEGORY).size());
	}

}
