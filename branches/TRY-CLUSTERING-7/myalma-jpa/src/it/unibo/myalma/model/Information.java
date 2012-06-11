package it.unibo.myalma.model;

import it.unibo.myalma.model.Content;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;

/**
 * Entity implementation class for Entity: Information
 *
 */
@Entity
@Access(FIELD)
@DiscriminatorValue("information")
public class Information extends Content implements Serializable {

	// Mappato in CLOB per dati di grandi dimensioni (caratteri)
	@Lob
	private String body;
	
	private static final long serialVersionUID = 1L;

	public Information() {
		super();
	}   
	
	public Information(ContentType type, User creator)
	{
		this(type, "","","",creator);
	}
	
	public Information(ContentType type, String title, String body, String description, User creator)
    {
		super(type, title,description,creator);
		
		if(type != ContentType.INFORMATION && type != ContentType.NOTICE)
			throw new IllegalArgumentException("Is not possible to create an information of type " + type.toString());
		
		this.body = body;
    }
	
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
