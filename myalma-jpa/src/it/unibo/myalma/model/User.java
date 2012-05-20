package it.unibo.myalma.model;

import java.io.Serializable;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import org.jboss.crypto.CryptoUtil;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="users")
@Access(FIELD)
public class User implements Serializable {

	//	@Id
	//	@GeneratedValue(strategy=GenerationType.AUTO)
	//	private int id;

	@Id
	@Column(nullable = false, unique = true)
	private String mail;

	@Column(nullable=false, unique = true)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;

	@ManyToMany(cascade = { MERGE, REFRESH }, fetch = EAGER)
	private Set<Role> roles = new HashSet<Role>();

	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}   	

	public User(String mail, String password, String name, String surname) {
		super();
		this.mail = mail;
		this.setPassword(password);
		this.name = name;
		this.surname = surname;
	}

	public User(String mail, String password, String name, String surname, Role[] roles) {
		this(mail,password,name,surname);
		for(Role role : roles)
		{
			this.roles.add(role);
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	} 

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}   
	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) 
	{
		// La password � memorizzata nel DB sotto forma di hash SHA
		this.password = CryptoUtil.createPasswordHash("SHA", CryptoUtil.RFC2617_ENCODING, "UTF-8", getMail(), password);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean hasRole(String roleName)
	{
		return getRoles().contains(new Role(roleName));
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(this == obj)
			return true;
		if( !(obj instanceof User) )
			return false;
		
		User user = (User)obj;
		return this.mail.equalsIgnoreCase(user.getMail());	// (pag. 398 Hibernate 2006)
	}
	
	@Override
	public int hashCode() 
	{
		return this.mail.hashCode();
	}
}