package it.unibo.myalma.business;

public interface IEditMaterial extends IEditContent 
{
	public void setFileInfo(String fileName, byte[] data);
	
	void setFileData(byte[] data);
	byte[] getFileData();
	
	void setFileName(String name);
	String getFileName();

}
