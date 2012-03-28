package it.unibo.myalma.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// TODO possibili problemi di concorrenza
public class FileManager 
{
	// TODO Per cluster forse bisogna cambiare percorso
	private String serverPath = "../server/default/data/myalma/";

	public String saveFile(String fileName, byte[] fileData)
	{
		String path = serverPath + fileName;
		try 
		{
			FileOutputStream stream = new FileOutputStream(path);
			stream.write(fileData);
			stream.close();

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return path;
	}
	
	public boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		return file.delete();
	}
	
	public String getServerPath() {
		return serverPath;
	}

	// Nel caso in cui si voglia modificare a runtime
	protected void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
}
