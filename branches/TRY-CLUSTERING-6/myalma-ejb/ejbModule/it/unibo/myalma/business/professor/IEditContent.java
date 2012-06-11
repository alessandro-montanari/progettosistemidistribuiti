package it.unibo.myalma.business.professor;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;

public interface IEditContent 
{	
	void init();
	
	// Binding methods -----------------------------------------
	// Dopo aver settato l'id del contenuto si deve chiamare edit()
	void setContentId(int id);
	int getContentId();
	Content getContent();
	
	void setParentContentId(int parentId);						
	int getParentContentId();									
	Content getParentContent();
	
	// Business methods -----------------------------------------
	void newContent(ContentType type);
	void save();	
	void cancel();
	void delete();
	void edit();
	void saveInSession();
	
	void destroy();
}
