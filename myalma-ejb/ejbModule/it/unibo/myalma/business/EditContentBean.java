package it.unibo.myalma.business;

import java.lang.reflect.Method;

import it.unibo.myalma.model.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
 
/**
 * Session Bean implementation class ContentManagerBean
 */

@Stateful
@Name("contentManager")
@Scope(ScopeType.CONVERSATION)
@Remote(IEditContent.class)
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

	// E se il client � remoto (stand-alone)
	// In realt� io potrei utilizzare qui un contesto classico, dato che di fatto non mi serve esteso qui, mi serve esteso nella
	// interfaccia web, per sempleficarmi il lavoro, quindi lo posso utilizzare esteso via Seam direttamente
	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private IProfessorManager profManager;

	@Resource
	private SessionContext context;

	public EditContentBean() 
	{
		// TODO Costruttore bean
	}

	@PostConstruct
	private void init()
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

//	@Override
//	public void edit()
//	{
//		if(content == null)
//			throw new IllegalStateException("Impossible to edit a null content");
//
//		entityManager.detach(content);	// Mi assicuro che il content sia detached perch� devo riportare le modifiche sul DB solo al salvataggio
//	}

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
			// Il contenuto non c'� nel DB quindi � nuovo e deve essere aggiunto
			contentId = profManager.appendContent(parentContent.getId(), content);
		}
		else if(contentDB.getParentContent().getId() != parentContent.getId())
		{
			// Il contenuto � gi� presente nel DB ed � stato spostato 
			profManager.removeContent(contentDB.getParentContent().getId(), contentDB.getId());
			contentId = profManager.appendContent(parentContent.getId(), content);
		}
		else
		{
			// Il contenuto � gi� presente nel DB e NON � stato spostato, quindi solo aggiornamento propriet�
			profManager.updateContent(content);
			contentId = content.getId();
		}
		
		events.raiseTransactionSuccessEvent("contentSaved",contentId);
	
	}

	@Override
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
		content = entityManager.find(Content.class, contentId);
		if(content != null)
		{																// Inizializzo il parent che mi servir� nella UI
			parentContent = content.getParentContent();
			parentContent.getTitle();
		}
	}

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
	
	@Override
	public void setContent(Content content) 
	{
		this.content = content;
	}

	@Remove @Destroy
	public void cancel() 
	{
//		events.raiseTransactionSuccessEvent("operationCancelled",content);
	}

	@Override
	public void delete() 
	{
		if(content == null)
			throw new IllegalStateException("Impossible to delete a null content");
		if(parentContent == null)
			throw new IllegalStateException("Impossible to delete a content from a null category");
		
		profManager.removeContent(parentContent.getId(), content.getId());
		
		events.raiseTransactionSuccessEvent("contentDeleted",content);
	}
}