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
import javax.persistence.PersistenceContext;

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
		return entityManager.createQuery("SELECT u FROM User u, IN( u.roles ) r WHERE r.roleName='professor'").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<User> findProfessorsByName(String name) 
	{
		return entityManager.createQuery("SELECT u FROM User u WHERE u.name LIKE :name")
				.setParameter("name", name)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> getAllTeachings() 
	{
		return entityManager.createQuery("SELECT t FROM Teaching t").getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public Teaching findTeachingByName(String name) 
	{
		return (Teaching) entityManager.createQuery("SELECT t FROM Teaching t WHERE t.name LIKE :name")
				.setParameter("name", name)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByYear(int year) {
		return entityManager.createQuery("SELECT t FROM Teaching t WHERE t.yearOfCourse=?")
				.setParameter(1, year)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByYearRange(int year1, int year2) {
		return entityManager.createQuery("SELECT t FROM Teaching t WHERE t.yearOfCourse>=?1 and t.yearOfCourse<=?2")
				.setParameter(1, year1)
				.setParameter(2, year2)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsBySsd(String ssd) {
		return entityManager.createQuery("SELECT t FROM Teaching t WHERE t.ssd=?")
				.setParameter(1, ssd)
				.getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "professor", "admin"})
	public List<User> getAllStudents() 
	{
		return entityManager.createQuery("SELECT u FROM User u, IN( u.roles ) r WHERE r.roleName='student'").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<Content> getAllContents() 
	{
		// Usare sempre il nome della classe nelle query e non il nome della tabella in cui è mappata la classe
		return entityManager.createQuery("SELECT c FROM Content c").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<User> getAllUsers() 
	{
		return entityManager.createQuery("SELECT u FROM User u").getResultList();
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
		return entityManager.createQuery("SELECT u FROM Subscriber u, IN ( u.subscriptions ) sub WHERE sub.teaching.id=?")
						.setParameter(1, teachingId)
						.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<User> findStudentsSubscribedToTeaching(String teachingName) 
	{
		return entityManager.createQuery("SELECT u FROM Subscriber u, IN ( u.subscriptions ) sub WHERE sub.teaching.name=?")
				.setParameter(1, teachingName)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByEditorId(String profId) 
	{
		return entityManager.createQuery("SELECT t FROM Teaching t WHERE t.contentsRoot.editor.id=?")
						.setParameter(1, profId)
						.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByEditorName(String profName) 
	{
		return entityManager.createQuery("SELECT t FROM Teaching t WHERE t.contentsRoot.editor.name=?")
				.setParameter(1, profName)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByAssistantId(String profId) 
	{
		return entityManager.createQuery("SELECT t FROM Teaching t, IN (t.contentsRoot.authors) auth WHERE auth.id=?")
				.setParameter(1, profId)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Teaching> findTeachingsByAssistantName(String profName) 
	{
		return entityManager.createQuery("SELECT t FROM Teaching t, IN (t.contentsRoot.authors) auth WHERE auth.name=?")
				.setParameter(1, profName)
				.getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public Teaching findTeachingByContentsRoot(int rootId) 
	{
		return (Teaching) entityManager.createQuery("SELECT t FROM Teaching t WHERE t.contentsRoot.id=?")
						.setParameter(1, rootId)
						.getSingleResult();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public Teaching findTeachingByContentsRoot(String rootTitle) 
	{
		return (Teaching) entityManager.createQuery("SELECT t FROM Teaching t WHERE t.contentsRoot.title=?")
				.setParameter(1, rootTitle)
				.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Content> findContentsByTitle(String title) 
	{
		return entityManager.createQuery("SELECT c FROM Content c WHERE c.title LIKE ?")
				.setParameter(1, title)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "student", "admin"})
	public List<Content> findContentsByType(ContentType type) 
	{
		return entityManager.createQuery("SELECT c FROM Content c WHERE c.contentType=?")
				.setParameter(1, type)
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
		return entityManager.createQuery("SELECT r FROM Role r").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({"admin"})
	public List<Notification> getAllNotifications() 
	{
		return entityManager.createQuery("SELECT n FROM Notification n").getResultList();
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
		return entityManager.createQuery("SELECT sub FROM Subscriber u, IN ( u.subscriptions ) sub WHERE u.mail=?")
				.setParameter(1, mail)
				.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<Subscription> findSubscriptionsToTeaching(String name) 
	{
		return entityManager.createQuery("SELECT sub FROM Subscription sub WHERE sub.teaching.name=?")
				.setParameter(1, name)
				.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed({ "professor", "admin"})
	public List<Subscription> findSubscriptionsToTeaching(int teachingId) 
	{
		return entityManager.createQuery("SELECT sub FROM Subscription sub WHERE sub.teaching.id=?")
				.setParameter(1, teachingId)
				.getResultList();
	}

	@Override
	@RolesAllowed({ "professor", "admin"})
	public User findUserBySubscription(int subscriptionId) 
	{
		return (User) entityManager.createQuery("SELECT s FROM Subscriber s, IN ( s.subscriptions ) sub WHERE sub.id=?")
						.setParameter(1, subscriptionId)
						.getSingleResult();
	}
}
