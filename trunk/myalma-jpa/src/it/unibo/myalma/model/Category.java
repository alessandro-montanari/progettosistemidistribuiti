package it.unibo.myalma.model;

import it.unibo.myalma.model.Content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * Entity implementation class for Entity: Category
 *
 */
@Entity
@Access(FIELD)
@DiscriminatorValue("category")
public class Category extends Content implements Serializable {

	// se orphanRemoval fosse true non appena un Content viene eliminato da contents il relativo Entity viene anche rimosso dal DB, quindi non sarebbe possibile
	// appendere quel contenuto ad un'altra categoria (infatti errore di entity eliminato). In questo modo però potrebbero rimanere dei contenuti fluttuanti nel DB
	// quindi occorre fare molta attenzione alle operazioni che si compione nei session bean e/o prevedere dei bot che periodicamente ripuliscono il DB da questi contenuti
	// ---
	// fetch deve essere eager altrimenti si aveva un errore durante la rimozione di un contenuto
	@OneToMany(orphanRemoval = true, cascade = ALL, fetch = LAZY, mappedBy = "parentContent")
	private List<Content> contents = new ArrayList<Content>();
	
	private static final long serialVersionUID = 1L;

	public Category() {
		super();
	}   
	
	public Category(String title, String description, User creator)
	{
		super(ContentType.CATEGORY, title,description,creator);
	}

	public Category(User creator)
	{
		super(ContentType.CATEGORY,creator);
	}
	
	@Override
	protected boolean isContainer() 
	{
		return true;
	}
	
	@Override
	public Content appendContent(Content aContent)
	{
		if (!this.isContainer())
            throw new UnsupportedOperationException("Impossible to append a content to a non-category content");
        
		// Non è possibile agggiungere and un ContentsRoot nessun contenuto che non sia una categoria
        if (this.getContentType() == ContentType.CONTENTS_ROOT && aContent.getContentType() != ContentType.CATEGORY)
        	throw new UnsupportedOperationException("Impossible to append a non-category content to a ContentsRoot content");
        
        // Il contenuto è già presente in un altro albero (rimuoverlo da lì prima)
        if (aContent.getRoot() != null)
        	throw new IllegalStateException("Impossible to append " + aContent.getTitle() + " because it belongs to another tree. Remove from there first.");

        // Il contenuto è già presente in un altro albero (rimuoverlo da lì prima)
        if (aContent.getParentContent() != null)
        	throw new IllegalStateException("Impossible to append " + aContent.getTitle() + " because it belongs to another tree. Remove from there first.");

        aContent.setParentContent(this);
        aContent.setRoot(this.getContentType() == ContentType.CONTENTS_ROOT ? (ContentsRoot)this : this.getRoot());
        
        contents.add(aContent);

        return aContent;
	}
	
//	@Override
//	public Content replaceContent(Content newContent, Content oldContent)
//	{
//		this.removeContent(oldContent);
//        this.appendContent(newContent);
//
//        return oldContent;
//	}
	
	@Override
	public Content removeContent(Content theContent)
	{
		if (!this.isContainer())
			throw new UnsupportedOperationException("Impossible to remove a content from a non-category content");

        theContent.setParentContent(null);
        theContent.setRoot(null);

        contents.remove(theContent);

        return theContent;
	}
	
	@Override
	public void removeAllContents()
	{
		for (Content cont : contents) 
		{
			removeContent(cont);
		}
	}
	
	@Override
	public List<Content> getChildContents() 
	{
		return new ArrayList<Content>(contents);
	}
   
}
