package it.unibo.myalma.model;

import it.unibo.myalma.model.Teaching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;

/**
 * Entity implementation class for Entity: Subscription
 *
 */
@Entity
@Table(name="subscriptions")
@Access(AccessType.FIELD)
public class Subscription implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@ManyToOne(cascade = REFRESH)
	private Teaching teaching;
	
	// In cascade non c'è ne MERGE ne PERSIST perché le notifiche devono essere uniche per ogni evento, ossia se un
	// contenuto viene aggiunto c'è una sola tupla che rappresenta la notifica che sarà poi collegata con tutte le
	// sottoscrizioni relative al corso a cui appartiene il contenuto interessato.
	// Per motivo analogo non c'è neanche REMOVE
	@ManyToMany(cascade = REFRESH, fetch = LAZY)
	private List<Notification> unreadNotifications = new ArrayList<Notification>();
	
	private static final long serialVersionUID = 1L;

	public Subscription() {
		super();
	}   
	
	public Subscription(Teaching teaching)
	{
		this.teaching = teaching;
	}
	
	public int getId() {
		return this.id;
	}

	public Teaching getTeaching() {
		return this.teaching;
	}

	protected void setTeaching(Teaching teaching) {
		this.teaching = teaching;
	}   
	
	public List<Notification> getUnreadNotifications() {
		return this.unreadNotifications;
	}

	protected void setUnreadNotifications(List<Notification> unreadNotifications) {
		this.unreadNotifications = unreadNotifications;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(this == obj)
			return true;
		if( !(obj instanceof Subscription) )
			return false;
		
		Subscription sub = (Subscription)obj;
		return this.teaching.equals(sub.getTeaching()) && this.unreadNotifications.equals(sub.getUnreadNotifications());
	}
	
	@Override
	public int hashCode() 
	{
		return this.teaching.hashCode();
	}
}
