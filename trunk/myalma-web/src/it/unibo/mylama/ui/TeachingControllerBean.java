package it.unibo.mylama.ui;

import java.util.ArrayList;
import java.util.List;

import it.unibo.myalma.business.IEditContent;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Teaching;

import javax.faces.event.FacesEvent;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.richfaces.model.TreeRowKey;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.component.state.TreeState;


@Name("teachingControllerBean")
@Scope(ScopeType.CONVERSATION)
public class TeachingControllerBean 
{

	// Entity manager esteso alla conversazione corrente e gestito da Seam
	// Faccio l'Out così lo posso usare in EditContentBean
	@In
	@Out(scope=ScopeType.CONVERSATION,value="ExtendedPersistenceContext")
	private EntityManager entityManager;
	
	@In
	private Events events;

	private int teachingId = -1;
	private Content contentsRoot = null;
	private Content selectedContent = null;
	private TreeNode<Content> root = null;
	
	private IEditContent contentManager = null;

	private final String dummyKey = "dummy";
	private final TreeNodeImpl<Content> dummyElement = new TreeNodeImpl<Content>();
	
	@Observer("contentDeleted")
	public void contentDeletedListener(Content content)
	{
// -------------------------------------------------------------
//		Content parent = entityManager.merge(parentContent);
//		entityManager.refresh(parent);
// -------------------------------------------------------------
		
// -------------------------------------------------------------
//		entityManager.getEntityManagerFactory().getCache().evict(Content.class, parentContent.getId());
//		entityManager.getEntityManagerFactory().getCache().evictAll();
//		entityManager.refresh(entityManager.merge(parentContent));
// -------------------------------------------------------------
		
// -------------------------------------------------------------
//		Content contentMerged = entityManager.merge(content);
//		entityManager.refresh(contentMerged);
// -------------------------------------------------------------
		
// -------------------------------------------------------------
//			Session session = (Session)entityManager.getDelegate();
//			session.evict(content);
//			entityManager.refresh(content.getParentContent());
// -------------------------------------------------------------
		
// -------------------------------------------------------------
//			entityManager.flush();
// -------------------------------------------------------------		
		
// -------------------------------------------------------------
//			entityManager.detach(content);
// -------------------------------------------------------------
			
// -------------------------------------------------------------
//			content.getParentContent().removeContent(content);
//			entityManager.flush();
// -------------------------------------------------------------
			
// -------------------------------------------------------------
//			content.getParentContent().removeContent(content);
//			entityManager.refresh(content.getParentContent());
// -------------------------------------------------------------
		
		// Questo codice lancia un'eccezione che non sembra fare danni
		// TODO possibile punto debole per performance (pesante accesso al DB)
		Content contentDB = entityManager.find(Content.class, content.getId());
		
		if(contentDB.getParentContent().getContentType().equals(ContentType.CONTENTS_ROOT))
			this.root = null;
		
		entityManager.refresh(contentDB);
		
		
	}
	
	@Observer("contentSaved")
	public void contentSavedListener(int contentId)
	{
		// In questo modo si riduce l'accesso al DB ma non mi piace molto dal punto di visto logico
		// dato che viene fatta una nuova scrittura con ciò che è già stato scritto prima (quindi forse 
		// solo lettura e nessuna vera scrittura)
//		entityManager.merge(content);
		
		// TODO possibile punto debole per performance (pesante accesso al DB)
		Content contentDB = entityManager.find(Content.class, contentId);
		entityManager.refresh(contentDB);
	}

	public int getTeachingId()
	{
		return teachingId;	
	}

	public void setTeachingId(int id)
	{
		teachingId = id;
		contentsRoot = entityManager.find(Teaching.class, teachingId).getContentsRoot();
	}

	public Content getContentsRoot()
	{
		return contentsRoot;
	}

	// Per ogni richiesta, questo metodo viene chiamato diverse volte quindi occhio ;)
	public TreeNode<Content> getRootNode()
	{
		if(root == null)
		{
			root = new TreeNodeImpl<Content>();
			TreeNode<Content> node = new TreeNodeImpl<Content>();
			node.setData(contentsRoot);
			node.addChild(dummyKey, dummyElement);
			node.setParent(root);
			root.addChild(contentsRoot.getId(), node);
		}

		return root;
	}

	private TreeNode<Content> extractNode(FacesEvent event)
	{
		// get the source or the component who fired this event.  
		Object source = event.getComponent(); 
		if (source instanceof UITree) 
		{  
			// It should be a html tree node, if yes get  
			// the ui tree which contains this node.  
			UITree tree = (HtmlTree)source;
			// avoid null pointer exceptions even though not needed. but safety first ;-)  
			if (tree == null) 
			{  
				return null;  
			}  
			// get the row key i.e. id of the given node.  
			TreeRowKey rowKey = (TreeRowKey)tree.getRowKey(); 

			// get the model node of this node.  
			return (TreeNode<Content>)tree.getModelTreeNode(rowKey);  
		}

		return null;
	}

	public void selectionChanged(NodeSelectedEvent event)
	{
		TreeNode<Content> selectedTreeModelNode = extractNode(event);
		if(selectedTreeModelNode == null)
			return;

		setSelectedContent(selectedTreeModelNode.getData());
	}

	public void nodeToggled(NodeExpandedEvent event) 
	{
		TreeNode<Content> selectedTreeModelNode = extractNode(event);
		if(selectedTreeModelNode == null)
			return;

		UITree tree = (HtmlTree)event.getComponent();  

		TreeState state = (TreeState) tree.getComponentState();
		if (!(state.isExpanded((TreeRowKey) tree.getRowKey()))) 
		{
			return;
		}
		else
		{

			// Se c'è l'elemento dummy significa che non ho mail popolato questo nodo quindi lo popolo,
			// se non c'è, l'ho già popolato ed evito di ripopolarlo (e se qualcuno aggiunge qualcosa nel frattempo?)
			//				if(selectedTreeModelNode.getChild(dummyKey) != null)
			//				{

			selectedTreeModelNode.removeChild(dummyKey);
			if(null != selectedTreeModelNode)
			{  
				for(Content content : selectedTreeModelNode.getData().getChildContents())
				{
					if(content.getContentType().equals(ContentType.CATEGORY))
					{
						TreeNodeImpl newNode = new TreeNodeImpl();  
						newNode.setData(content);  
						newNode.setParent(selectedTreeModelNode);  
						newNode.addChild(dummyKey, dummyElement);
						selectedTreeModelNode.addChild(content.getId(), newNode);  
					}
				}
			}
			//				}
		}

	}

	public Content getSelectedContent() 
	{
		return selectedContent;
	}

	public void setSelectedContent(Content selectedContent) 
	{
//		if(contentManager == null)
//		{
//			// Così non viene creata l'istanza ma solo cercata nel contesto specificato, se non c'è null
//			// TODO vedere di utilizzare una bean factory (così che sia EditContent che EditMaterial abbiano lo stesso nome) 
//			// o utlizzare gli eventi, altrimenti la modifica del parent non arriva a EditMaterial
//			contentManager = (IEditContent) Contexts.getConversationContext().get("contentManager");
//		}
//		
//		// Se si sta modificando un contenuto, riporto il cambiamento della categoria padre
//		if(contentManager != null)
//		{
//			contentManager.setParentContentId(selectedContent.getId());
//		}
		
		events.raiseTransactionSuccessEvent("parentContentSelectionChanged",selectedContent.getId());
		
		
		
		// TODO Molto pesante per il DB
//		entityManager.refresh(selectedContent);
		
		// Sarebbe meglio farlo solo su evento del contentManager (eventi seam)
//		entityManager.refresh(this.contentsRoot);
//		entityManager.detach(selectedContent);
//		entityManager.merge(selectedContent);
		
		this.selectedContent = selectedContent;
	}

	// TODO Sembra che questo metodo sia chiamato una volta per ogni contenuto presente nella categoria selezionata !??!?!?!
	public List<Content> getChildsOfSelectedContent()
	{
		List<Content> result = new ArrayList<Content>();

		// Deve essere selezionato qualcosa
		if(selectedContent != null)
		{
			for(Content content : selectedContent.getChildContents())
			{
				if(!(content.getContentType().equals(ContentType.CATEGORY)) && !(content.getContentType().equals(ContentType.CONTENTS_ROOT)))
				{
					result.add(content);
				}
			}
		}

		return result;
	}
}
