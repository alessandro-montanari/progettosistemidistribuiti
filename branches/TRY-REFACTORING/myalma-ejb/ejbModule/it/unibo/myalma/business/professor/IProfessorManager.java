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
	Content removeContent(Content parent, Content content);
	void removeAllContents(Content parent);

	void updateContent(int contentId, String whatToModify, String newValue);
	
	// Metodo di comodo: aggiorna solo le proprietˆ del contenuto ma ignora eventuali modifiche rigurdanti il parent e il root (per questi utilizzare
	// i metodi append e remove del bean)
	// Utile quando si modificano diverse proprietˆ in un colpo solo
	void updateContent(Content content);
}
