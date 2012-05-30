package it.unibo.myalma.test;

import static org.junit.Assert.*;

import java.io.File;

import it.unibo.myalma.business.professor.IEditMaterial;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.test.helpers.AutomateImportMYSQL;
import it.unibo.myalma.test.helpers.TestHelper;
import it.unibo.myalma.business.util.FileManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EditMaterialBeanTestCase {
	
	static TestHelper helper = new TestHelper();
	static AutomateImportMYSQL automator = new AutomateImportMYSQL();
	static ISearch searchBean = null;
	static IEditMaterial editMaterialBean = null;
	// DA MODIFICARE
	static String serverPath = "/Users/ale/Documents/Magistrale/Anno2/SistemiDistribuiti/Strumenti/jboss-6.1.0.Final/server/default/data/myalma/";

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
		editMaterialBean.setFileInfo(fileName, new byte[]{34, 54, 65});
		editMaterialBean.setParentContentId(searchBean.findContentsByType(ContentType.CATEGORY).get(0).getId());
		editMaterialBean.save();
		assertEquals(previousMaterials+1, searchBean.findContentsByType(ContentType.MATERIAL).size());
		File file = new File(serverPath + fileName);
		assertTrue(file.exists());
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditMaterialBean() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFileData() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileData() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFileInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFileName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileName() {
		fail("Not yet implemented");
	}

}
