package it.unibo.myalma.model;

import java.io.Serializable;
import java.lang.String;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: NotificationPolicy
 *
 */
@Entity
@Table(name="notificationPolicies")
public class NotificationPolicy implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String notifier;
	
	@ElementCollection
	private Map<String, Boolean> settings = new Hashtable<String, Boolean>();
	
	private static final long serialVersionUID = 1L;

	public NotificationPolicy() {
		super();
	}   
	
	public NotificationPolicy(String notifier, Map<String, Boolean> settings) 
	{
		this.notifier = notifier;
		this.settings = settings;
	}
	
	public NotificationPolicy(String notifier) 
	{
		this(notifier, new Hashtable<String, Boolean>());
	}

	public int getId() {
		return this.id;
	}

	public String getNotifier() {
		return this.notifier;
	}

	protected void setNotifier(String notifier) {
		this.notifier = notifier;
	}
	
	public Map<String, Boolean> getSettings() {
		return this.settings;
	}

	protected void setSettings(Map<String, Boolean> settings) {
		this.settings = settings;
	}   
}
