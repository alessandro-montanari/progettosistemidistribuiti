package it.unibo.myalma.test;

import static org.junit.Assert.*;

import java.io.File;

import it.unibo.myalma.business.professor.IEditMaterial;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Material;
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
public class EditMaterialBeanTestCase {
	
	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IEditMaterial editMaterialBean = null;
	// DA MODIFICARE
	static String serverPath = "/Users/ale/Documents/Magistrale/Anno2/SistemiDistribuiti/Strumenti/jboss-6.1.0.Final/server/default/data/myalma/";
	static byte[] data = new byte[] {12, 34, 67};
	
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
		editMaterialBean = (IEditMaterial)helper.lookup("myalma-ear/EditMaterialBean/remote");
		editMaterialBean.init();
	}
	
	@After
	public void tearDownTest()
	{
		editMaterialBean.destroy();
		helper.logout();
	}

	@Test
	public void testSave() 
	{
		int previousMaterials = searchBean.findContentsByType(ContentType.MATERIAL).size();
		String fileName = "test-file.dat";
		editMaterialBean.newContent(ContentType.MATERIAL);
		editMaterialBean.setFileInfo(fileName, data);
		editMaterialBean.setParentContentId(searchBean.findContentsByType(ContentType.CATEGORY).get(0).getId());
		editMaterialBean.save();
		assertEquals(previousMaterials+1, searchBean.findContentsByType(ContentType.MATERIAL).size());
		File file = new File(serverPath + fileName);
		assertTrue(file.exists());
	}
	
	@Test
	public void testSaveWithError() 
	{
		int previousMaterials = searchBean.findContentsByType(ContentType.MATERIAL).size();
		String fileName = "test-file.dat";
		editMaterialBean.newContent(ContentType.MATERIAL);
		editMaterialBean.setFileInfo(fileName, data);
		editMaterialBean.save();	// Scatterà un eccezione perché non è possibile appendere un contenuto ad una categoria null
		
		// Controllo che il materiale non sia inserito e che il file non sia salvato
		assertEquals(previousMaterials, searchBean.findContentsByType(ContentType.MATERIAL).size());
		File file = new File(serverPath + fileName);
		assertFalse(file.exists());
	}

	@Test
	public void testDelete() 
	{
		int previousMaterials = searchBean.findContentsByType(ContentType.MATERIAL).size();
		Material material = (Material)searchBean.findContentsByTitle("Materiale").get(0);
		editMaterialBean.setContentId(material.getId());
		editMaterialBean.edit();
		editMaterialBean.delete();
		assertEquals(previousMaterials-1, searchBean.findContentsByType(ContentType.MATERIAL).size());
		File file = new File(material.getPath());
		assertFalse(file.exists());
	}
	
	@Test
	public void testFileData()
	{
		editMaterialBean.setFileData(data);
		assertEquals(data[0], editMaterialBean.getFileData()[0]);
		assertEquals(data[1], editMaterialBean.getFileData()[1]);
		assertEquals(data[2], editMaterialBean.getFileData()[2]);
	}
	
	@Test
	public void testFileInfo()
	{
		editMaterialBean.setFileInfo("test", data);
		assertEquals("test", editMaterialBean.getFileName());
		assertEquals(data[0], editMaterialBean.getFileData()[0]);
		assertEquals(data[1], editMaterialBean.getFileData()[1]);
		assertEquals(data[2], editMaterialBean.getFileData()[2]);
	}
	
	@Test
	public void testFileName()
	{
		editMaterialBean.setFileInfo("test", data);
		assertEquals("test", editMaterialBean.getFileName());
	}
}
