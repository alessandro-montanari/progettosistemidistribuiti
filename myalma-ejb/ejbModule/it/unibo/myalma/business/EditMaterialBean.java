package it.unibo.myalma.business;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import it.unibo.myalma.model.Material;

@Stateful
@Name("materialManager")
@Scope(ScopeType.CONVERSATION)
@Remote(IEditMaterial.class)
@RolesAllowed({"professor", "admin"})
public class EditMaterialBean extends EditContentBean implements IEditMaterial 
{
	private byte[] fileData = null;
	private String fileName = "";
	private FileManager fileManager = null;

	public EditMaterialBean()
	{
		fileManager = new FileManager();
	}

	@Override
	public void setFileData(byte[] data) 
	{
		fileData = data;
	}

	@Override
	public byte[] getFileData() 
	{
		return fileData;
	}

	@Override
	public void setFileInfo(String fileName, byte[] data) 
	{
		setFileName(fileName);
		setFileData(data);
	}

	@Override
	public void setFileName(String name) 
	{
		fileName = name;
	}

	@Override
	public String getFileName() 
	{
		return fileName;
	}

	@Override
	public void save() 
	{
		Material material = (Material)this.getContent();
		
		//Controllo se c'� gi� un file collegato al conteuto
		File file = new File(material.getPath());
		if(file.exists())
			fileManager.deleteFile(material.getPath());
		
		// Devo salvare il file prima di salvare il contenuto nel DB perch� mi serve il path
		String path = fileManager.saveFile(fileName, fileData);
		material.setPath(path);
		
		try
		{
			super.save();
		}
		catch(Exception e)
		{
			// Se il salvataggio non va a buon fine elimino il file appena inserito per non lasciare file volanti
			fileManager.deleteFile(path);
		}
	}

	@Override
	public void delete() 
	{
		Material material = (Material)this.getContent();
		fileManager.deleteFile(material.getPath());
		super.delete();
	}
}
