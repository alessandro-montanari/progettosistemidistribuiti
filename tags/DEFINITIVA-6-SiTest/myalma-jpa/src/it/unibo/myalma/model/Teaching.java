package it.unibo.myalma.model;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.CascadeType.PERSIST;

/**
 * Entity implementation class for Entity: Teaching
 *
 */
@Entity
@Table(name="teachings")
@Access(FIELD)
public class Teaching implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(nullable = false)
	private int cfu;
	
	@Column(nullable = false)
	private int yearOfCourse;
	
	@Column(nullable = false)
	private String ssd;
	
	// Per semplicità assumiamo che non ci possano essere insegnamenti con lo stesso nome
	@Column(nullable = false, unique = true)
	private String name;
	
	// Una cosa è l'insegnamento e una cosa è l'abero dei contenuti, qui rappresentato dalla sua radice
	@OneToOne(optional = false, cascade = { MERGE, REFRESH, REMOVE, PERSIST })
	private ContentsRoot contentsRoot;
	
	private static final long serialVersionUID = 1L;

	public Teaching() {
		super();
	}   
	
	public Teaching(int cfu, int yearOfCourse, String ssd, String name, ContentsRoot contentsRoot) {
		this.cfu = cfu;
		this.yearOfCourse = yearOfCourse;
		this.ssd = ssd;
		this.name = name;
		this.contentsRoot = contentsRoot;
	}
	public Teaching(int cfu, int yearOfCourse, String ssd, String name) {
		this(cfu, yearOfCourse, ssd, name, null);
	}
	
	public int getCfu() {
		return this.cfu;
	}

	protected void setCfu(int cfu) {
		this.cfu = cfu;
	}   
	
	public int getYearOfCourse() {
		return this.yearOfCourse;
	}

	protected void setYearOfCourse(int yearOfCourse) {
		this.yearOfCourse = yearOfCourse;
	}   
	
	public String getSsd() {
		return this.ssd;
	}

	protected void setSsd(String ssd) {
		this.ssd = ssd;
	}   
	
	public String getName() {
		return this.name;
	}

	protected void setName(String name) {
		this.name = name;
	}   
	
	public int getId() {
		return this.id;
	}
	
	public ContentsRoot getContentsRoot() {
		return contentsRoot;
	}
	
	public void setContentsRoot(ContentsRoot contentsRoot) {
		this.contentsRoot = contentsRoot;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(this == obj)
			return true;
		if( !(obj instanceof Teaching))
			return false;
		
		Teaching teaching = (Teaching)obj;
		return this.name.equalsIgnoreCase(teaching.getName());
	}
	
	@Override
	public int hashCode() 
	{
		return this.name.hashCode();
	}
}
