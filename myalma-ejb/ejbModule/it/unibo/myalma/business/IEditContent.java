package it.unibo.myalma.business;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;

public interface IEditContent 
{

	// Binding methods
	void setContentId(int contentId);
	int getContentId();
	Content getContent();
	void setContent(Content content);

	
	void setParentContentId(int parentId);						// Serve per l'aggiunta di nuovi contenuti, per sapere dove inserirlo
	int getParentContentId();									// in caso di modifica di un contenuto già esistente questo è ignorato
	Content getParentContent();
	
	
	// Business methods
	void newContent(ContentType type);
//	void edit();
	void updateContent(String whatToModify, String newValue); 	// Questo metodo si usa con client remoti dato che ricevono una copia del content
																// i client locali (Seam) invece utilizzano lo stesso riferimento a content mantenuto
																// dal bean, quindi modificano lo stesso entity (che dovrà essere detached)
	void save();	
	void cancel();
	void delete();
}
