package it.unibo.myalma.business;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;

public interface IEditContent 
{	
	//@Create
	void init();
	
	// Binding methods -----------------------------------------
	void setContentId(int contentId);
	int getContentId();
	Content getContent();
	
	void setParentContentId(int parentId);						
	int getParentContentId();									
	Content getParentContent();
	
	
	// Business methods -----------------------------------------
	void newContent(ContentType type);
	void updateContent(String whatToModify, String newValue); 	// Questo metodo si usa con client remoti dato che ricevono una copia del content
																// i client locali (Seam) invece utilizzano lo stesso riferimento a content mantenuto
																// dal bean, quindi modificano lo stesso entity (che dovrˆ essere detached)
	void save();	
	void cancel();
	void delete();
}
