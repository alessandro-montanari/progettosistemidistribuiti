package it.unibo.myalma.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ContentsRoot
 *
 */
@Entity
@DiscriminatorValue("contents_root")
public class ContentsRoot extends Category implements Serializable {

	
	@ManyToOne(cascade = { MERGE, REFRESH })
	private User editor;
	
	@ManyToMany(cascade = { MERGE, REFRESH })
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
