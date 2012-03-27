package it.unibo.mylama.ui;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.event.NodeExpandedEvent;
import org.richfaces.model.TreeNode;


@Name("lazyTreeBean")
@Scope(ScopeType.APPLICATION)
public class LazyTreeBean 
{
	Content root = null;
	
	@In
	private EntityManager entityManager;

	public void init()
	{
		User u = entityManager.find(User.class, "alessandro.montanar5@studio.unibo.it");
		root = new Category(u.getName(),"",null);
		root.appendContent(new Category("Cat2","",null));
		root.appendContent(new Category("Cat3","",null));
		root.appendContent(new Category("Cat4","",null));
		Category cat = new Category("Cat5","",null);
		cat.appendContent(new Information(ContentType.NOTICE,"Info","","",null));
		root.appendContent(cat);
		for(int i = 0; i< 30; i++)
		{
			Category cate = new Category("cat"+i,"",null);
			cat.appendContent(cate);
			cat = cate;
		}
		
	}
	
	
	public void selectionChanged(NodeSelectedEvent event)
	{
		UITree tree = (UITree) event.getComponent();
        TreeNode currentNode = tree.getModelTreeNode(tree.getRowKey());
		
	}
	
	public void nodeToggled(NodeExpandedEvent event) 
	{
		UITree tree = (UITree) event.getComponent(); 
		TreeNode modelNode = (TreeNode)tree.getRowData();
		
	}
	
	public List<Content> getRoot()
	{
		if(root == null)
			init();
		
		List<Content> list = new ArrayList<Content>();
		list.add(root);
		return list;
	}
	
	




}
