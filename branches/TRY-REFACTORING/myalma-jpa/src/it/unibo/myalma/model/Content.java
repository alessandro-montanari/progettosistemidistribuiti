package it.unibo.myalma.model;

import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.jboss.seam.annotations.Name;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.FetchType.EAGER;
/**
 * Entity implementation class for Entity: Content
 *
 * Note: 
 * - questa classe non è @MappedResource perché altrienti non sarebbe possibile fare delle query su di essa e collegarla ad altre
 * 	entity tramite relazioni, quindi si è deciso di renderla abstract (anche perché si era obbligati dato che il campo ContentType)
 * 	può essere definito solo dalle sottoclassi), in questo modo la classe può essere annotata @Entity.
 * - si è deciso per un InheritanceType.SINGLE_TABLE per due motivi:
 * 	1) si pensa che la maggiorparte delle query eseguite saranno di tipo polimorfico e interesseranno tutta la gerarchia (ossia si
 * 	vuole fare una query su Content per avere tutti i contenuti dell'albero o di una prte di esso), quindi il vantaggio di avere una
 * 	sola tabella è evidente (si risparmiano diversi JOIN rispetto alle altre strategie);
 * 	2) lo svantaggio dato dalla scarsa efficienza nell'utilizzo dell spazio di questa strategia (molti campi nulli) è limitato dal
 * 	fatto che le informazioni di ogni sotto classe sono abbastanza limitate (path, body, ecc.) 
 */
@Entity
@Table(name="contents")
@Access(FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@Name("content")
public abstract class Content implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id = -1;
	
	@Column(nullable = false)
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, nullable = false)
	private Date creationDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false, nullable = false)
	private Date modificationDate;
	
	@ManyToOne(optional = false, cascade = { MERGE, REFRESH })
	private User creator;
	
	@ManyToOne(optional = false, cascade = { MERGE, REFRESH })
	private User modifier;
	
	private String description;
	
	@ManyToOne(cascade = { REFRESH, MERGE }, fetch = LAZY)
	private Content parentContent;
	
	@OneToOne(cascade = { MERGE, REFRESH }, fetch = LAZY)
	private ContentsRoot root;
	
	@Column(nullable = false)
	@Enumerated(STRING)
	private ContentType contentType;
	
	private static final long serialVersionUID = 1L;

	public Content() {
		super();
	}
	
	public Content(ContentType type, String title, String description, User creator)
    {
		this.contentType = type;
		this.creator = creator;
		this.modifier = creator;
		this.title = title;
		this.description = description;
		//Contenuto "fluttuante" (quando si farà persist si dovrà controllare che il contenuto non si "fluttuante")
        this.parentContent = null; 
        this.root = null;
    }
	
	public Content(ContentType type, User creator)
	{
		this.contentType = type;
		this.creator = creator;
		this.modifier = creator;
	}

	public int getId() {
		return id; 
	}

	public Content getParentContent() {
		return parentContent;
	}

	protected void setParentContent(Category parentContent) {
		this.parentContent = parentContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}   
	
	public ContentsRoot getRoot() {
		return root;
	}

	protected void setRoot(ContentsRoot root) 
	{
		this.root = root;
		for (Content cont : getChildContents()) 
		{
			cont.setRoot(root);
		}
	}

	@Override
	public String toString()
	{
		return getTitle() + " - " + getModifier() + " - " + getModificationDate();
	}
	
	
	public Content appendContent(Content aContent)
	{
		throw new UnsupportedOperationException("Impossible to append a content to a non-category content");
	}
	
	public Content removeContent(Content theContent)
	{
		throw new UnsupportedOperationException("Impossible to remove a content from a non-category content");
	}
	
	public void removeAllContents()
	{
		throw new UnsupportedOperationException("Impossible to remove al contents from a non-category content");
	}
	
	public List<Content> getChildContents()
	{
		return new ArrayList<Content>();
	}
	
	protected boolean isContainer()
	{
		return false;
	}

	
	public ContentType getContentType() {
		return contentType;
	}
	
	protected void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	// TODO Occhio potrebero esserci problemi
//	@Override
//	public boolean equals(Object obj) 
//	{
//		if(this == obj)
//			return true;
//		if( !(obj instanceof Content))
//			return false;
//		
//		// E quando si arriva alla radice che ha parentContent = null; ??
//		Content content = (Content)obj;
//		if( !(this.title.equalsIgnoreCase(content.getTitle())) )
//			return false;
//		if( !(this.parentContent.equals(content.getParentContent())))
//			return false;
//		
//		return true;
//	}
//	
//	@Override
//	public int hashCode() {
//		// TODO Auto-generated method stub
//		return super.hashCode();
//	}
}
