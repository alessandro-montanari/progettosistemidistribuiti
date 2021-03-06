package it.unibo.myalma.business.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

// TODO possibili problemi di concorrenza
public class FileManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serverPath = "";
	
	public FileManager()
	{
		serverPath = System.getProperty("jboss.server.data.dir");
		serverPath += File.separator + "myalma" + File.separator;
	}

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
	
	public boolean exists(String filename)
	{
		File file = new File(serverPath + filename);
		boolean res = file.exists();
		return res;
	}

	// Nel caso in cui si voglia modificare a runtime
	protected void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
}
