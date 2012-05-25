package it.unibo.myalma.model;

import it.unibo.myalma.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

/**
 * Note:
 * 	- i concetti di User e Role sono del tutto generali e possono essere riutilizzati in diverse applicazioni, siccome 
 * 	però in questa specifica c'è il bisogno di associare l'utente ad un'altra entità (il corso, teaching) per poter
 * 	ricevere le notifiche riguardanti i contenuti aggiunti al corso, si è subclassato User inserendo nella sottoclasse 
 * 	un'insieme di sottoscrizioni, ciascuna delle quali riguarda un solo Teaching.
 *
 */
@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("subscriber")
public class Subscriber extends User implements Serializable {

	@OneToMany(cascade = { MERGE, REFRESH, REMOVE, PERSIST }, orphanRemoval = true, fetch = LAZY)
	private Set<Subscription> subscriptions = new HashSet<Subscription>();
	
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
	
	public Set<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}  
}
