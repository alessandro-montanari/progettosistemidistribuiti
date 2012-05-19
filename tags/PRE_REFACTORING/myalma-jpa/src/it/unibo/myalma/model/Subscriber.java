package it.unibo.myalma.model;

import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.CascadeType.PERSIST;

/**
 * Entity implementation class for Entity: Subscriber
 *
 */
@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("subscriber")
public class Subscriber extends User implements Serializable {

	@OneToMany(cascade = { MERGE, REFRESH, REMOVE, PERSIST }, orphanRemoval = true)
	private List<Subscription> subscriptions = new ArrayList<Subscription>();
	
	private static final long serialVersionUID = 1L;

	public Subscriber() {
		super();
	}   
	
	public Subscriber(String mail, String password, String name, String surname) {
		super(mail,password,name,surname);
	}
	
	public Subscriber(String mail, String password, String name, String surname, Role[] roles) {
		super(mail,password,name,surname,roles);
	}
	
	public List<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	
   
}
