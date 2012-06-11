package it.unibo.myalma.business.professor;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;


public interface IProfessorManager 
{
	User updatePersonalInfo(String whatToModify, String newValue);
	
	void addAssistant(Teaching teachingId, User assistant);
	void removeAssistant(Teaching teachingId, User assistant);

	Content appendContent(Content parent, Content content);
	
	// La chiave primaria del contenuto passato viene utilizzata per identificarlo univocamente nel DB
	Content removeContent(Content content);
	
	// Dato che il titolo dei contenuti non è univoco nel DB deve essere passato anche il parent da cui rimuoverlo
	Content removeContent(Content parent, String contentTitle);
	
	void removeAllChildContents(Content parent);

	Content updateContent(int contentId, String whatToModify, String newValue);
	
	// Metodo di comodo: aggiorna solo le proprietà del contenuto ma ignora eventuali modifiche rigurdanti il parent e il root 
	// (per questi utilizzare si metodi append e remove del bean)
	// Utile quando si modificano diverse proprietà in un colpo solo
	Content updateContent(Content content);
}
