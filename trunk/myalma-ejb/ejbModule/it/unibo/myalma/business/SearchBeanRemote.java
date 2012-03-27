package it.unibo.myalma.business;

import java.util.List;

import javax.ejb.Remote;

import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;

@Remote
public interface SearchBeanRemote 
{
	List<User> getAllProfessors();
	List<User> findProfessorsByName(String name);

	List<User> getAllStudents();
	List<User> findAllStudentsSubscribedToTeaching(int teachingId);
	List<User> findAllStudentsSubscribedToTeaching(String teachingName);
	
	List<Teaching> getAllTeachings();
	Teaching findTeachingById(int id);
	Teaching findTeachingByName(String name);
	List<Teaching> findTeachingsByYear(int year);
	List<Teaching> findTeachingsByYearRange(int year1, int year2);
	List<Teaching> findTeachingsBySsd(String ssd);
	
	List<Teaching> findAllTeachingsByEditorId(String profId);
	List<Teaching> findAllTeachingsByEditorName(String profName);
	List<Teaching> findAllTeachingsByAssistantId(String profId);
	List<Teaching> findAllTeachingsByAssistantName(String profName);
	
	List<User> findAllAssistantsByTeaching(int teachingId);
	List<User> findAllAssistantsByTeaching(String teachingName);
	
	Teaching findTeachingByContentsRoot(int rootId);
	Teaching findTeachingByContentsRoot(String rootTitle);
	
	List<Content> getAllContents();
	List<User> getAllUsers();
	List<Role> getAllRoles();
	List<Notification> getAllNotifications();
	
	Content findContentById(int id);
	List<Content> findContentsByTitle(String title);
	List<Content> findContensByType(ContentType type);
	// TODO Mancano i finder per i contenuti e per le sottoscrizioni
}
