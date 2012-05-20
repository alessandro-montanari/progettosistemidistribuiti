package it.unibo.myalma.business.search;

import java.util.List;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Subscription;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;


public interface ISearch 
{
	List<User> getAllProfessors();
	List<User> findProfessorsByName(String name);

	List<User> getAllStudents();
	List<User> findStudentsSubscribedToTeaching(int teachingId);
	List<User> findStudentsSubscribedToTeaching(String teachingName);
	
	List<Teaching> getAllTeachings();
	Teaching findTeachingById(int id);
	Teaching findTeachingByName(String name);
	List<Teaching> findTeachingsByYear(int year);
	List<Teaching> findTeachingsByYearRange(int year1, int year2);
	List<Teaching> findTeachingsBySsd(String ssd);
	
	List<Teaching> findTeachingsByEditorId(String profId);
	List<Teaching> findTeachingsByEditorName(String profName);
	List<Teaching> findTeachingsByAssistantId(String profId);
	List<Teaching> findTeachingsByAssistantName(String profName);
	
	List<User> findAssistantsByTeaching(int teachingId);
	List<User> findAssistantsByTeaching(String teachingName);
	
	Teaching findTeachingByContentsRoot(int rootId);
	Teaching findTeachingByContentsRoot(String rootTitle);
	
	List<Content> getAllContents();
	List<User> getAllUsers();
	List<Role> getAllRoles();
	List<Notification> getAllNotifications();
	
	Content findContentById(int id);
	List<Content> findContentsByTitle(String title);
	List<Content> findContentsByType(ContentType type);
	
	List<Subscription> findSubscriptionsByUserId(String mail);
	List<Subscription> findSubscriptionsToTeaching(String name);
	List<Subscription> findSubscriptionsToTeaching(int teachingId);
	
	User findUserBySubscription(int subscriptionId);
}
