package it.unibo.myalma.model;

import it.unibo.myalma.model.Teaching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;

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
	private int id;
	
	@ManyToOne(cascade = { MERGE, REFRESH })
	private Teaching teaching;
	
	// TODO OrphanRemoval ??
//	@OneToMany(cascade = ALL)
//	private List<NotificationPolicy> policies = new ArrayList<NotificationPolicy>();
	
	@ManyToMany(cascade = { MERGE, REFRESH, PERSIST })
	private List<Notification> unreadNotifications = new ArrayList<Notification>();
	
//	@ManyToMany(cascade = { REFRESH, MERGE, PERSIST })
//	private List<Notification> readNotifications = new ArrayList<Notification>();
	
	private static final long serialVersionUID = 1L;

	public Subscription() {
		super();
	}   
	
//	public Subscription(Teaching teaching, List<NotificationPolicy> policies)
//	{
//		this.policies = policies;
//		this.teaching = teaching;
//	}
	
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
	
//	public List<Notification> getReadNotifications() {
//		return readNotifications;
//	}
//	
//	protected void setReadNotifications(List<Notification> readNotifications) {
//		this.readNotifications = readNotifications;
//	}
	
//	public List<NotificationPolicy> getPolicies() {
//		return policies;
//	}
//	
//	protected void setPolicies(List<NotificationPolicy> policies) {
//		this.policies = policies;
//	}
   
}
