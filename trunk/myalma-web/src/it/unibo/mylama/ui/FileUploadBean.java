package it.unibo.mylama.ui;

import it.unibo.myalma.business.IEditContent;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerator.Feature;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.richfaces.event.FileUploadListener;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

// Vedi opzioni in web.xml per dimensione massima e salvataggio in cartella temporanea
@Name("fileUploadBean")
@Scope(ScopeType.CONVERSATION)
public class FileUploadBean implements FileUploadListener
{

	private ArrayList<UploadItem> files = new ArrayList<UploadItem>();
	
	private List<String> uploadedFiles = new ArrayList<String>();
	private Content currentContent = null;

	public void processUpload(UploadEvent event) 
	{
		UploadItem item = event.getUploadItem();
		String path = "../server/default/data/myalma/"+item.getFileName();
		try 
		{
			// Con "./"+item.getFileName() il file viene scritto in jboss-6.1.0.Final/bin
			// TODO Per cluster forse bisogna cambiare percorso
		
			FileOutputStream stream = new FileOutputStream(path);
			stream.write(item.getData());
			stream.close();

		} catch (IOException e) 
		{
			e.printStackTrace();
		}

		if(currentContent == null)
		{
			// Cos“ non viene creata l'istanza ma solo cercata nel contesto specificato, se non c' null
			currentContent = (Content) Contexts.getConversationContext().get("currentContent");
		}
		
		// Se si sta modificando un contenuto, riporto il cambiamento della categoria padre
		if(currentContent != null)
		{
			if(currentContent.getContentType().equals(ContentType.MATERIAL))
			{
				Material material = (Material)currentContent;
				material.setPath(path);
			}
		}
		
		files.add(item);
		uploadedFiles.add(path);
	}
	
	@Observer({"contentDeleted", "operationCancelled"})
	public void deleteReferencedFile(Content content)
	{
		Material material = (Material)content;
		File file = new File(material.getPath());
		file.delete();
	}
	
	@Observer("contentSaved")
	public void contentSaved(int contentId)
	{
		
	}

	public int getSize() 
	{
		if (getFiles().size()>0){
			return getFiles().size();
		}else 
		{
			return 0;
		}
	}

	public ArrayList<UploadItem> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadItem> files) { 
		this.files = files;
	}

	public void paint(OutputStream stream, Object object) throws IOException 
	{

	}

	public long getTimeStamp()
	{
		return System.currentTimeMillis();
	}

	public String clearUploadData() 
	{
		files.clear();
		return null;
	}

}
