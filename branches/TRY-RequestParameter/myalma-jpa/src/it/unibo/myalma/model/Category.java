package it.unibo.myalma.model;

import it.unibo.myalma.model.Content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

/**
 * Note:
 * 	- vedi Content.java per note su Lazy Inizialization
 * 
 * 	- abilito anche l'orphanRemoval così sono sicuro che quando un contenuto viene rimosso, è anche rimosso completamente dal DB,
 * 	non rimangono contenuti volanti. E' un po' inefficiente qundo un contenuto deve essere spostato perché viene rimosso e poi
 * 	reinserito, quando invece sarebbe bastato modificare un valore nella riga corrispondere a quel contenuto.
 *
 */
@Entity
@Access(FIELD)
@DiscriminatorValue("category")
public class Category extends Content implements Serializable {

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
	
	@Override
	public Content removeContent(Content theContent)
	{
		if (!this.isContainer())
			throw new UnsupportedOperationException("Impossible to remove a content from a non-category content");

		// Libero il contenuto da questo albero
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
		return contents;
	}
}
