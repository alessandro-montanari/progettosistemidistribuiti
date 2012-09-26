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
@NamedQueries(
{
	@NamedQuery(name= "findTeachingsByEditorId",
				query="SELECT t FROM Teaching t WHERE t.contentsRoot.editor.mail=:id",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsByEditorName",
				query="SELECT t FROM Teaching t WHERE t.contentsRoot.editor.name=:name",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsByAssistantId",
				query="SELECT t FROM Teaching t, IN (t.contentsRoot.authors) auth WHERE auth.mail=:id",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsByAssistantName",
				query="SELECT t FROM Teaching t, IN (t.contentsRoot.authors) auth WHERE auth.name=:name",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingByContentsRootId",
				query="SELECT t FROM Teaching t WHERE t.contentsRoot.id=:id",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingByContentsRootTitle",
				query="SELECT t FROM Teaching t WHERE t.contentsRoot.title=:title",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingByName",
				query="SELECT t FROM Teaching t WHERE t.name LIKE :name",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "getAllTeachings",
				query="SELECT t FROM Teaching t",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsByYear",
				query="SELECT t FROM Teaching t WHERE t.yearOfCourse=:year",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsByYearRange",
				query="SELECT t FROM Teaching t WHERE t.yearOfCourse>=:year1 and t.yearOfCourse<=:year2",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name= "findTeachingsBySsd",
				query="SELECT t FROM Teaching t WHERE t.ssd=:ssd",
				hints={ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
})
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

	// Per semplicitˆ assumiamo che non ci possano essere insegnamenti con lo stesso nome
	@Column(nullable = false, unique = true)
	private String name;

	// Una cosa  l'insegnamento e una cosa  l'abero dei contenuti, qui rappresentato dalla sua radice
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
