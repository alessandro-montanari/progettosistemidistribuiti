package it.unibo.myalma.model;

import static javax.persistence.CascadeType.REFRESH;
import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

/**
 * Note:
 * 	- Fino a Category si ha una struttura del tutto generale, con questa classe invece viene inserito il concetto esplicito di radice
 * 	dell'albero e il fatto che un albero (= la sua radice) ha un editore e diersi autori. I termini editore e autori sono stati 
 * 	utilizzati invece di DocenteTitolare e Assistenti per avere un modello quanto pi� possibile non cablato sul dominio di questa 
 * 	specifica applicaztione, potendolo cos� utilizzare un domani in un'altra.
 *
 */
@Entity
@DiscriminatorValue("contents_root")
public class ContentsRoot extends Category implements Serializable {

	@ManyToOne(cascade = { REFRESH }, optional = false)
	private User editor;
	
	// Se metessi MERGE come cascade otterei che oltre a poter modificare le info di un docente passando per un contenuto,
	// potrei inserire un nuovo docente nel DB semplicemnete aggiungendolo ad authors e questo NON va bene dato che
	// solo l'amministratore pu� farlo (utilizzando opportuni metodi)
	@ManyToMany(cascade = { REFRESH }, fetch = LAZY)
	@JoinTable(joinColumns = @JoinColumn(name = "contents_id", referencedColumnName = "id"))
	private Set<User> authors = new HashSet<User>();
	
	private static final long serialVersionUID = 1L;

	public ContentsRoot() {
		super();
	}   
	
	public ContentsRoot(String title, User creator)
	{
		super(creator);
		this.setContentType(ContentType.CONTENTS_ROOT);
		this.setTitle(title);
		this.setDescription("");
	}
	
	public User getEditor() {
		return this.editor;
	}
	
	public void setEditor(User editor) {
		this.editor = editor;
	}
	
	public Set<User> getAuthors() {
		return this.authors;
	}
	
	public void setAuthors(Set<User> authors) {
		this.authors = authors;
	}
}
