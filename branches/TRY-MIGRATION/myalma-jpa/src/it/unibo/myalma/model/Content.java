package it.unibo.myalma.model;

import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

/**
 * Entity implementation class for Entity: Content
 *
 * Note: 
 * - questa classe non è @MappedResource perché altrienti non sarebbe possibile fare delle query su di essa e collegarla ad altre
 * 	entity tramite relazioni (non c'è una tabella per una @MappedSuperclass), quindi si è deciso di renderla abstract, 
 * 	in questo modo la classe può essere annotata @Entity.
 * 
 * - si è deciso per un InheritanceType.SINGLE_TABLE per due motivi:
 * 	1) si pensa che la maggiorparte delle query eseguite saranno di tipo polimorfico e interesseranno tutta la gerarchia (ossia si
 * 	vuole fare una query su Content per avere tutti i contenuti dell'albero o di una prte di esso), quindi il vantaggio di avere una
 * 	sola tabella è evidente (si risparmiano diversi JOIN rispetto alle altre strategie);
 * 	2) lo svantaggio dato dalla scarsa efficienza nell'utilizzo dell spazio di questa strategia (molti campi nulli) è limitato dal
 * 	fatto che le informazioni di ogni sotto classe sono abbastanza limitate (path, body, ecc.);
 * 	Alla fine è quasi scontato che si utilizzi questo mapping per un albero in cui si vuole navigare utilizzando solo la classe più
 * 	in alto nella gerarchia.
 * 
 * - Si è utilizzato @Access(FIELD) per poter avere accesso nel mondo a oggetti in sola letura (solo getXXX())
 * 
 * - Come discriminatore non posso utilizzare il campo contentType (sarebbe logico) perché poter essere un discriminatore 
 * 	dovrebbe avere come impostazioni insert="false" e update="false" altrimenti errore a deploy time. Non posso però mettere
 * 	l'impostazione insert="false" perché altrimenti non riesco ad inserire nessun contenuto nel DB (a run time) dato che quel
 * 	campo non può essere "generato" dal DB stesso (come avviene per modificationDate) ma lo devo inserire io.
 * 
 * - Scelta du Lazy Inizialization: è stato deciso che il cliente del layer EJB sarà il web tier, quindi è possibile sfruttare 
 * 	i contesti di persistenza estesi e quindi la possibilità di avere Lazy Inizialization (la grande potenza/vantaggio di un sistema
 * 	come JPA). In seguito a questa scelta si è deciso che ogni nodo dell'albero viene restituito co le sole sue proprietà, ossia 
 * 	senza precaricare ne i suoi nodi figli ne il suo parent (Lazy Inizialization "massimo"), evitando così che per una richiesta
 * 	di un nodo venga caricato l'intero albero. All'interno del web tier utilizzando un contesto di persistenza esteso, non si avrà
 * 	nessun tipo di problema e anzi si semplificherà la programmazione (non si devono gestire manualmente le LazyInizializationExceptions).
 * 	Al momento non sono stati previsti clienti remoti diretti all'EJB tier ma anche se questi dovessero essere aggiunti in futuro
 * 	le classi del modello rimarrebbero con Lazy Inizialization e poi sarà necessaria una diversa implementazione del SearchBean
 * 	che dovrà almeno popolare "una parte dell'albero nell'intorno" del nodo richiesto e una certa disciplina nella programmazione
 * 	dei client evitando/gestendo le LazyInizializationExceptions.
 */
@Entity
@Table(name="contents")
@Access(FIELD)																		
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Content implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id = -1;
	
	@Column(nullable = false)
	private String title;
	
	// Campo mappato come java.sql.Timestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, nullable = false)
	private Date creationDate = new Date();
	
	// Uso un frammento SQL tipico di MYSQL (quindi app non cross-DB vendor) per ottenere l'aggiornamento
	// automatico della data di modifica. Si potrebbe fare in altri modi per rendere l'app cross-DB vendor.
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", 
			insertable = false, updatable = false, nullable = false)
	private Date modificationDate;
	
	// Per modificare le proprietà di un docente occorre utilizzare il 
	// metodo ProfessorManagerBean.updatePersonalInfo(...) quidi cascade solo a REFRESH
	@ManyToOne(optional = false, cascade = REFRESH)
	private User creator;
	
	@ManyToOne(optional = false, cascade = REFRESH)
	private User modifier;
	
	private String description;
	
	@ManyToOne(cascade = { REFRESH, MERGE }, fetch = LAZY)
	private Content parentContent;
	
	@OneToOne(cascade = { MERGE, REFRESH }, fetch = LAZY)
	private ContentsRoot root;
	
	// Enumeration salvato sul DB come stringa
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
		this(type, "", "", creator);
	}

	public int getId() {
		return id; 
	}
	
//	private void setId(int id) {
//		this.id = id; 
//	}

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
	
	// Metodi di "modifica" dell'albero saranno implementati dalla classe Category
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
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Content content = (Content)super.clone();
		// Per non avere entity con stesso id in giro :)
		content.id = -1;
		content.root = null;
		content.parentContent = null;
		return content;
	}
}
