package it.unibo.myalma.business.professor;

import it.unibo.myalma.model.Content;


public interface IProfessorManager 
{
	// In questo modo faccio un giro (rete) per ogni dato da modificare che è meglio dato che raramente si modificano tutti i dati in una volta
	void updatePersonalInfo(String whatToModify, String newValue);

	void addAssistant(int teachingId, String assistantMail);
	void removeAssistant(int teachingId, String assistantMail);

	Content appendContent(int parentId, Content content);
	Content removeContent(int parentId, int contentId);
	void removeAllContents(int parentId);

	// In questo modo faccio un giro (rete) per ogni dato da modificare che è meglio dato che raramente si modificano tutti i dati in una volta
	void updateContent(int contentId, String whatToModify, String newValue);
	
	// Metodo di comodo: aggiorna solo le proprietà del contenuto ma ignora eventuali modifiche rigurdanti il parent e il root (per questi utilizzare
	// i metodi append e remove del bean)
	// Utile quando si modificano diverse proprietà in un colpo solo
	void updateContent(Content content);
}
