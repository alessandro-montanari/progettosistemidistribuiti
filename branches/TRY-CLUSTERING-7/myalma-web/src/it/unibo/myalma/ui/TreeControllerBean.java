package it.unibo.myalma.ui;

import java.io.Serializable;
import java.util.List;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Teaching;

import javax.faces.event.FacesEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.richfaces.model.TreeRowKey;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.component.state.TreeState;

@Name("treeControllerBean")
@Scope(ScopeType.CONVERSATION)
public class TreeControllerBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private EntityManager entityManager;
	
	@In
	private Events events;

	private int teachingId = -1;
	private Content contentsRoot = null;
	private Content selectedContent = null;
	private TreeNode<Content> root = null;

	private final String dummyKey = "dummy";
	private final TreeNodeImpl<Content> dummyElement = new TreeNodeImpl<Content>();

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
			// Devo inserire un dummyElement, altrimenti non viene visualizzata la freccina per espandere il nodo
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
				return null;  
			
			// get the row key i.e. id of the given node.  
			TreeRowKey rowKey = (TreeRowKey)tree.getRowKey(); 

			// get the model node of this node.  
			return (TreeNode<Content>)tree.getModelTreeNode(rowKey);  
		}

		return null;
	}

	// Metodo registrato come listener del cambio di selezione
	public void selectionChanged(NodeSelectedEvent event)
	{
		TreeNode<Content> selectedTreeModelNode = extractNode(event);
		if(selectedTreeModelNode == null)
			return;

		setSelectedContent(selectedTreeModelNode.getData());
	}

	// Metodo registrato come listener di espansione di un nodo
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
			selectedTreeModelNode.removeChild(dummyKey);
			if(null != selectedTreeModelNode)
			{  
				for(Content content : selectedTreeModelNode.getData().getChildContents())
				{
					if(content.getContentType().equals(ContentType.CATEGORY))
					{
						TreeNodeImpl<Content> newNode = new TreeNodeImpl<Content>();  
						newNode.setData(content);  
						newNode.setParent(selectedTreeModelNode);  
						newNode.addChild(dummyKey, dummyElement);
						selectedTreeModelNode.addChild(content.getId(), newNode);  
					}
				}
			}
		}
	}

	public Content getSelectedContent() 
	{
		return selectedContent;
	}

	public void setSelectedContent(Content selectedContent) 
	{	
		// Notifico che la selezione del parent è cambiata, queto evento viene catturato da EditContentBean solo se si sta modificando
		// un contenuto (nota il create=false in @Observer su setParentContentId())
		events.raiseTransactionSuccessEvent("parentContentSelectionChanged",selectedContent.getId());
		
		this.selectedContent = selectedContent;
	}

	// Questo metodo viene chiamato una volta per ogni contenuto presente nella categoria selezionata
	// quinidi implementazione più leggera possibile
	public List<Content> getChildsOfSelectedContent()
	{
		if(selectedContent != null)
		{
			return this.selectedContent.getChildContents();
		}
		
		return null;
	}
}
