package it.unibo.myalma.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/**
 * Entity implementation class for Entity: Notification
 *
 */
@Entity
@Table(name = "notifications")
@Access(AccessType.FIELD)
public class Notification implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(nullable = false)
	private String message;
	
	int contentId;

	@Enumerated(STRING)
	@Column(nullable = false)
	private TypeOfChange changeType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false, nullable = false)
	private Date date;
	
	private static final long serialVersionUID = 1L;

	public Notification() {
		super();
	}   
	
	// Costruisce una notifica relativa ad un contenuto che NON è più presente nel DB, in seguito ad una rimozione di un
	// contenuto. Il campo contentId è valorizzato a -1 perché ovviamente il contenuto non esiste più.
	public Notification(String message, TypeOfChange changeType) {
		this(message,-1,changeType);
	}
	
	// Costruisce una notifica relativa ad un contenuto PRESENTE nel DB, in seguito ad esempio ad un'aggiunta o ad una 
	// modifica di un contenuto già esistente. Viene valorizzato oppurtunamente anche il campo contentId così che sia poi
	// possibile accedere al contenuto avendo in mano la notifica relativa
	public Notification(String message, int contentId, TypeOfChange changeType) {
		super();
		this.message = message;
		this.changeType = changeType;
		this.contentId = contentId;
	}

	public int getId() {
		return this.id;
	}
	
	public int getContentId() {
		return contentId;
	}
	
	public TypeOfChange getChangeType() {
		return changeType;
	}

	public Date getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}
}
