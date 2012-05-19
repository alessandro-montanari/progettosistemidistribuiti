package it.unibo.myalma.model;

import it.unibo.myalma.model.Content;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

/**
 * Entity implementation class for Entity: Notification
 *
 */

/*
 * Con questa tabella sarebbe anche possibile costruire la storia di ogni contenuto
 * Ma è troppo pesante per il DB ?
 */

@Entity
@Table(name = "notifications")
@Access(AccessType.FIELD)
public class Notification implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String message;
	
//	@ManyToOne(cascade = {MERGE, REFRESH })
//	private Content content;
	
	int contentId;

	@Enumerated(STRING)
	private TypeOfChange changeType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false, nullable = false)
	private Date date;
	
	private static final long serialVersionUID = 1L;

	public Notification() {
		super();
	}   
	
	public Notification(String message, TypeOfChange changeType) {
		this(message,-1,changeType);
	}
	
	public Notification(String message, int contentId, TypeOfChange changeType) {
		super();
		this.message = message;
//		this.content = content;
		this.changeType = changeType;
		this.contentId = contentId;
	}

	public int getId() {
		return this.id;
	}

//	public Content getContent() {
//		return this.content;
//	}
	
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
