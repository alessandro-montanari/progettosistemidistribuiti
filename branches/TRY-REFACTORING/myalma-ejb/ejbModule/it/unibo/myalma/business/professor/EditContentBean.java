package it.unibo.myalma.business.professor;

import java.lang.reflect.Method;

import it.unibo.myalma.model.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;

import it.unibo.myalma.business.professor.IProfessorManager;
 
/**
 * Session Bean implementation class ContentManagerBean
 */

@Stateful
@Name("contentManager")
@Scope(ScopeType.CONVERSATION)
@Local(IEditContent.class)
@RolesAllowed({"professor", "admin"})
public class EditContentBean implements IEditContent 
{
	@In(value="currentContent", required=false, scope=ScopeType.CONVERSATION)
	@Out(value="currentContent", required=false, scope=ScopeType.CONVERSATION)
	private Content content;
	
	@In(value="currentParentContent", required=false, scope=ScopeType.CONVERSATION)
	@Out(value="currentParentContent", required=false, scope=ScopeType.CONVERSATION)
	private Content parentContent = null;
	
	@In
	private Events events;

	private User user = null;

	// Qui utilizzo lo stesso contesto utilizzato in TeachingController così che tutto ciò che carica lui
	// io da qui non lo devo ricaricare (e viceversa) (ad esempio ogni volta che seleziono un diverso nodo padre)
	@In(scope=ScopeType.CONVERSATION, value="ExtendedPersistenceContext", create=false)
	@Out(scope=ScopeType.CONVERSATION, value="ExtendedPersistenceContext")
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
	public void updateContent(String whatToModify, String newValue) 
	{
		if(newValue == null || newValue.equals(""))
			throw new IllegalArgumentException("New value can not be empty");

		try 
		{
			Class<? extends Content> contentClass = content.getClass();
			String methodName = "set" + whatToModify.substring(0, 1).toUpperCase() + whatToModify.substring(1);
			Method method;

			method = contentClass.getMethod(methodName, String.class);

			method.invoke(content, newValue);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The property " + whatToModify + " does not exists");
		}

		return;
	}

	@Override
	public void save() 
	{
		if(content == null)
			throw new IllegalStateException("Impossible to edit a null content");
		if(parentContent == null)
			throw new IllegalStateException("Impossible to append a content to a null category");
		
		Content contentDB = entityManager.find(Content.class, content.getId());
		int contentId = -1;
		
		if(contentDB == null)
		{
			// Il contenuto non c'è nel DB quindi è nuovo e deve essere aggiunto
			contentId = profManager.appendContent(parentContent, content).getId();
		}
		else if(contentDB.getParentContent().getId() != parentContent.getId())
		{
			// Il contenuto è già presente nel DB ed è stato spostato 
			
			// Il contenuto viene rimosso dal suo parent originale e inserito in quello nuovo
			profManager.removeContent(contentDB);
			contentId = profManager.appendContent(parentContent, content).getId();
		}
		else
		{
			// Il contenuto è già presente nel DB e NON è stato spostato, quindi solo aggiornamento proprietà
			profManager.updateContent(content);
			contentId = content.getId();
		}
		
		events.raiseTransactionSuccessEvent("contentSaved",contentId);
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

	
	public void setContentId(int contentId) 
	{	
		// Setto solo contenuti non nulli
		Content contentDB = entityManager.find(Content.class, contentId);
		if(contentDB != null)
		{	
			this.content = contentDB;
			// Inizializzo il parent che mi servirà nella UI
			parentContent = content.getParentContent();
			parentContent.getTitle();
		}
	}

	@Override
	public int getContentId() 
	{
		if(content == null)
			return -1;
		return this.content.getId();
	}

	@Override
	public Content getContent() 
	{
		return content;
	}

	@Override @Remove @Destroy
	public void cancel() 
	{ }

	@Override
	public void delete() 
	{
		if(content == null)
			throw new IllegalStateException("Impossible to delete a null content");
		if(parentContent == null)
			throw new IllegalStateException("Impossible to delete a content from a null category");
		
		profManager.removeContent(content);
		
		events.raiseTransactionSuccessEvent("contentDeleted",content.getId());
	}
}
