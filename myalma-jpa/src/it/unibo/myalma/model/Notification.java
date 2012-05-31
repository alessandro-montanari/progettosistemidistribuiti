package it.unibo.myalma.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

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
	
	public Notification(String message, TypeOfChange changeType) 
	{
		super();
		this.message = message;
		this.changeType = changeType;
	}

	public int getId() {
		return this.id;
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
