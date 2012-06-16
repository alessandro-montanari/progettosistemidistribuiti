package it.unibo.myalma.business.search;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Subscription;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.QueryHint;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("searchBean")
@Scope(ScopeType.STATELESS)
@Stateless
@Local(ISearch.class)
//@Remote(it.unibo.myalma.business.remote.ISearchRemote.class)
public class SearchBean implements ISearch 
{
	@PersistenceContext(unitName="myalma-jpa")
	private EntityManager entityManager;

	public SearchBean() { }

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> getAllProfessors() 
	{
		return entityManager.createNamedQuery("getAllProfessors").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> findProfessorsByName(String name) 
	{
		return entityManager.createNamedQuery("findProfessorsByName")
				.setParameter("name", name)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> getAllTeachings() 
	{
		return entityManager.createNamedQuery("getAllTeachings").getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public Teaching findTeachingByName(String name) 
	{
		return (Teaching) entityManager.createNamedQuery("findTeachingByName")
				.setParameter("name", name)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByYear(int year) {
		return entityManager.createNamedQuery("findTeachingsByYear")
				.setParameter("year", year)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByYearRange(int year1, int year2) {
		return entityManager.createNamedQuery("findTeachingsByYearRange")
				.setParameter("year1", year1)
				.setParameter("year2", year2)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsBySsd(String ssd) {
		return entityManager.createNamedQuery("findTeachingsBySsd")
				.setParameter("ssd", ssd)
				.getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "admin"})
	public List<User> getAllStudents() 
	{
		return entityManager.createNamedQuery("getAllStudents").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<Content> getAllContents() 
	{
		// Usare sempre il nome della classe nelle query e non il nome della tabella in cui è mappata la classe
		return entityManager.createNamedQuery("getAllContents").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<User> getAllUsers() 
	{
		return entityManager.createNamedQuery("getAllUsers").getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> findAssistantsByTeaching(int teachingId) 
	{
		Teaching teaching = entityManager.find(Teaching.class, teachingId);
		User[] users = teaching.getContentsRoot().getAuthors().toArray(new User[0]);
		return Arrays.asList(users);
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> findAssistantsByTeaching(String teachingName) 
	{
		Teaching teaching = this.findTeachingByName(teachingName);
		User[] users = teaching.getContentsRoot().getAuthors().toArray(new User[0]);
		return Arrays.asList(users);
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<User> findStudentsSubscribedToTeaching(int teachingId) 
	{
		return entityManager.createNamedQuery("findStudentsSubscribedToTeachingId")
						.setParameter("id", teachingId)
						.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<User> findStudentsSubscribedToTeaching(String teachingName) 
	{
		return entityManager.createNamedQuery("findStudentsSubscribedToTeachingName")
				.setParameter("name", teachingName)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByEditorId(String profId) 
	{
		return entityManager.createNamedQuery("findTeachingsByEditorId")
							.setParameter("id", profId)
							.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByEditorName(String profName) 
	{
		return entityManager.createNamedQuery("findTeachingsByEditorName")
				.setParameter("name", profName)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByAssistantId(String profId) 
	{
		return entityManager.createNamedQuery("findTeachingsByAssistantId")
				.setParameter("id", profId)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByAssistantName(String profName) 
	{
		return entityManager.createNamedQuery("findTeachingsByAssistantName")
				.setParameter("name", profName)
				.getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public Teaching findTeachingByContentsRoot(int rootId) 
	{
		return (Teaching) entityManager.createNamedQuery("findTeachingByContentsRootId")
						.setParameter("id", rootId)
						.getSingleResult();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public Teaching findTeachingByContentsRoot(String rootTitle) 
	{
		return (Teaching) entityManager.createNamedQuery("findTeachingByContentsRootTitle")
				.setParameter("title", rootTitle)
				.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Content> findContentsByTitle(String title) 
	{
		return entityManager.createNamedQuery("findContentsByTitle")
				.setParameter("title", title)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Content> findContentsByType(ContentType type) 
	{
		return entityManager.createNamedQuery("findContentsByType")
				.setParameter("type", type)
				.getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public Content findContentById(int id) 
	{
		return entityManager.find(Content.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<Role> getAllRoles() 
	{
		return entityManager.createNamedQuery("getAllRoles").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<Notification> getAllNotifications() 
	{
		return entityManager.createNamedQuery("getAllNotifications").getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public Teaching findTeachingById(int id) 
	{
		return entityManager.find(Teaching.class, id);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<Subscription> findSubscriptionsByUserId(String mail) 
	{
		return entityManager.createNamedQuery("findSubscriptionsByUserId")
				.setParameter("mail", mail)
				.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<Subscription> findSubscriptionsToTeaching(String name) 
	{
		return entityManager.createNamedQuery("findSubscriptionsToTeachingName")
				.setParameter("name", name)
				.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<Subscription> findSubscriptionsToTeaching(int teachingId) 
	{
		return entityManager.createNamedQuery("findSubscriptionsToTeachingId")
				.setParameter("id", teachingId)
				.getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public User findUserBySubscription(int subscriptionId) 
	{
		return (User) entityManager.createNamedQuery("findUserBySubscription")
						.setParameter("id", subscriptionId)
						.getSingleResult();
	}
}
