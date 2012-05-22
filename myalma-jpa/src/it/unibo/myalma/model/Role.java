package it.unibo.myalma.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Role
 *
 */
@Entity
@Table(name = "roles")
@Access(AccessType.FIELD)
public class Role implements Serializable {
	   
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(unique = true, nullable = false)
	private String roleName;
	
	private static final long serialVersionUID = 1L;

	public Role() {
		super();
	} 
	
	public Role(String roleName) {
		super();
		this.roleName = roleName;
	} 
	
	public int getId() {
		return this.id;
	}
 
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
   
	@Override
	public boolean equals(Object obj) 
	{
		if(this == obj)
			return true;
		if( !(obj instanceof Role) )
			return false;
		
		Role role = (Role)obj;
		return this.roleName.equalsIgnoreCase(role.getRoleName());	// Posso usare roleName perché è unique nel DB (pag. 398 Hibernate 2006)
	}
	
	@Override
	public int hashCode() 
	{
		return this.roleName.hashCode();
	}
}
