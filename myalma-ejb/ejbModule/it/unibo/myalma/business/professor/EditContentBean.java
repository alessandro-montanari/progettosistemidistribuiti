package it.unibo.myalma.business.professor;

import it.unibo.myalma.model.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;

import it.unibo.myalma.business.professor.IProfessorManager;
 
@Stateful
@Name("contentManager")
@Scope(ScopeType.CONVERSATION)
@Local(IEditContent.class)
@Remote(it.unibo.myalma.business.remote.IEditContentRemote.class)
@RolesAllowed({"professor", "admin"})
public class EditContentBean implements IEditContent 
{
//	@In(value="currentContent", required=false, scope=ScopeType.CONVERSATION)
//	@Out(value="currentContent", required=false, scope=ScopeType.CONVERSATION)
	private Content content = null;		
	
//	@In(value="currentParentContent", required=false, scope=ScopeType.CONVERSATION)
//	@Out(value="currentParentContent", required=false, scope=ScopeType.CONVERSATION)
	private Content parentContent = null;
	
//	@RequestParameter
	private Integer contentId;

	private User user = null;

	// Qui utilizzo lo stesso contesto utilizzato in TeachingController cos“ che tutto ci˜ che carica lui
	// io da qui non lo devo ricaricare (e viceversa) (ad esempio ogni volta che seleziono un diverso nodo padre)
//	@In
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@EJB
	private IProfessorManager profManager;

	@Resource
	private SessionContext context;

	public EditContentBean() 
	{ }

	@Create
	public void init()
	{
		user = entityManager.find(User.class, context.getCallerPrincipal().getName());
	}

	@Override
	public void newContent(ContentType type) 
	{
		if(type.equals(ContentType.CATEGORY))
			content = new Category(user);
		else if(type.equals(ContentType.INFORMATION))
			content = new Information(ContentType.INFORMATION, user);
		else if (type.equals(ContentType.NOTICE))
			content = new Information(ContentType.NOTICE, user);
		else if (type.equals(ContentType.MATERIAL))
			content = new Material(user);
		else throw new IllegalArgumentException("Impossible to create a content of type " + type);

		// mi assicuro che il parent sia null
		parentContent = null;
	}

	@Override
	public void save() 
	{
		if(content == null)
			throw new IllegalStateException("Impossible to edit a null content");
		if(parentContent == null)
			throw new IllegalStateException("Impossible to append a content to a null category");

		if(!(entityManager.contains(content)))
		{
			// Il contenuto non c' nel DB quindi  nuovo e deve essere aggiunto
			profManager.appendContent(parentContent, content).getId();
		}
		else if(content.getParentContent().getId() != parentContent.getId())
		{
			// Il contenuto  giˆ presente nel DB ed  stato spostato
			
			// Siccome il contenuto potrebbere essere stato modificato, prima dello spostamento salvo le modifiche
			content = profManager.updateContent(content);
			content = profManager.removeContent(content);
			Content clonato = null;
			try {
				clonato = (Content) content.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
			profManager.appendContent(parentContent, clonato).getId();
		}
		else
		{
			// Il contenuto  giˆ presente nel DB e NON  stato spostato, quindi solo aggiornamento proprietˆ
			profManager.updateContent(content);
		}
		
		entityManager.flush();
	}

	@Override
	@Observer(create=false, value="parentContentSelectionChanged")
	public void setParentContentId(int contentId) 
	{
		this.parentContent = entityManager.find(Content.class, contentId);
	}

	@Override
	public int getParentContentId() 
	{
		if(parentContent == null)
			return -1;
		return this.parentContent.getId();
	}

	@Override
	public Content getParentContent() 
	{
		return parentContent;
	}

	public void setContentId(int id) 
	{	
		// Il content da modificare pu˜ essere settato solo una volta
		if(content == null && id >0)
			contentId = id;
	}

	@Override
	public int getContentId() 
	{
		return contentId;
	}

	@Override
	public Content getContent() 
	{
		return content;
	}

	@Override 
	public void cancel() 
	{ 
		// Controllo se il content  managed (caso di contenuto giˆ esistente) e lo ripristino
		if(entityManager.contains(content))
			entityManager.refresh(content);
		
		// se il contenuto  stato creato nuovo non faccio niente (non esiste nell'persistence context)
	}
	
	@Remove @Destroy
	public void destroy()
	{}

	@Override
	public void delete() 
	{
		if(content == null)
			throw new IllegalStateException("Impossible to delete a null content");
		if(parentContent == null)
			throw new IllegalStateException("Impossible to delete a content from a null category");
		
		content = profManager.removeContent(content);
		entityManager.flush();
	}

	@Override
	public void edit() 
	{
		if(contentId != null && contentId > 0)
			content = entityManager.find(Content.class, contentId);
		
		if(content == null)
			throw new IllegalArgumentException("Impossible to find the content with id = " + contentId);
		else
			parentContent = content.getParentContent();
	}

	/* Non viene fatto niente esplicitamente ma quando viene invocato questo metodo, Seam fa l'injection dei due componenti (currentContent e
	 * currentParentContent) copiando al loro interno i valori inseriti dall'utente nell'interfaccia e poi ne fa l'outjection reinserendoli nella
	 * conversazione (quindi salvataggio).
	 */
	@Override
	public void saveInSession() 
	{}
}
