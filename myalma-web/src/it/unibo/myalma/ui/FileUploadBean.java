package it.unibo.myalma.ui;

import it.unibo.myalma.business.IEditContent;
import it.unibo.myalma.business.IEditMaterial;
import it.unibo.myalma.model.Content;

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
		
		IEditMaterial editMaterialBean = (IEditMaterial) Contexts.getConversationContext().get("materialManager");
		if(editMaterialBean != null)
			editMaterialBean.setFileInfo(item.getFileName(), item.getData());
	
	}
}
