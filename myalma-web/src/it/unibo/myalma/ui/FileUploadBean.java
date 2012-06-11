package it.unibo.myalma.ui;

import java.io.Serializable;

import it.unibo.myalma.business.professor.IEditMaterial;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.richfaces.event.FileUploadListener;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;


// Vedi opzioni in web.xml per dimensione massima e salvataggio in cartella temporanea
@Name("fileUploadBean")
@Scope(ScopeType.CONVERSATION)
public class FileUploadBean implements FileUploadListener, Serializable
{
	private static final long serialVersionUID = 1L;

	public void processUpload(UploadEvent event) 
	{
		UploadItem item = event.getUploadItem();
		
		IEditMaterial editMaterialBean = (IEditMaterial) Contexts.getConversationContext().get("materialManager");
		if(editMaterialBean != null)
			editMaterialBean.setFileInfo(item.getFileName(), item.getData());
	
	}
}
