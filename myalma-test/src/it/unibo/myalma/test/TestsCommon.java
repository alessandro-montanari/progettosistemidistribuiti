package it.unibo.myalma.test;

import java.util.List;

import it.unibo.myalma.business.IAdministration;
import it.unibo.myalma.business.IProfessorManager;
import it.unibo.myalma.business.search.ISearch;
import it.unibo.myalma.business.IStudentManager;
import it.unibo.myalma.model.Category;
import it.unibo.myalma.model.Content;
import it.unibo.myalma.model.ContentType;
import it.unibo.myalma.model.ContentsRoot;
import it.unibo.myalma.model.Information;
import it.unibo.myalma.model.Material;
import it.unibo.myalma.model.Notification;
import it.unibo.myalma.model.Role;
import it.unibo.myalma.model.Subscriber;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.User;
import it.unibo.myalma.test.helpers.TestHelper;

public abstract class TestsCommon 
{
	protected static TestHelper helper = null;
	protected static IProfessorManager profManager = null;
	protected static IAdministration adminBean = null;
	protected static ISearch searchBean = null;
	protected static IStudentManager studentManager = null;

	protected static Teaching teaching1 = null;
	protected static Teaching teaching2 = null;

	protected static final String editor1Name = "Silvano";
	protected static final String editor2Name = "Enrico";
	protected static final String assistant1Name = "Paolo";
	protected static final String assistant2Name = "Giuseppe";

	protected static final String teaching1Name = "Ricerca Operativa";
	protected static final String teaching2Name = "Linguaggi";

	protected static final String material1Name = "Materiale1 "+teaching1Name;
	protected static final String material1Path = "example-files/Interfaccia.gstencil";
	protected static final String material2Name = "Materiale2 "+teaching1Name;
	protected static final String material2Path = "example-files/Wired-siliconvalley.pdf";
	protected static final String category1Name = "Categoria1 "+teaching1Name;
	protected static final String category2Name = "Categoria2 "+teaching1Name;
	protected static final String category3Name = "Categoria3 "+teaching1Name;
	protected static final String info1Name = "Informazione1 "+teaching1Name;

	protected static final String material1NameBis = "Materiale1 "+teaching2Name;
	protected static final String material2NameBis = "Materiale2 "+teaching2Name;
	protected static final String category1NameBis = "Categoria1 "+teaching2Name;
	protected static final String category2NameBis = "Categoria2 "+teaching2Name;
	protected static final String info1NameBis = "Informazione1 "+teaching2Name;

	//	protected static int totalContents = -1;
	//	protected static int totalCategories = -1;
	//	protected static int totalMaterials = -1;
	//	protected static int totalInformations = -1;

	protected static void init()
	{
		helper = new TestHelper();
		profManager = (IProfessorManager)helper.lookup("myalma-ear/ProfessorManagerBean/remote");
		adminBean = (IAdministration)helper.lookup("myalma-ear/AdministrationBean/remote");
		searchBean = (ISearch)helper.lookup("myalma-ear/SearchBean/remote");
		studentManager = (IStudentManager)helper.lookup("myalma-ear/StudentManagerBean/remote");
	}

	protected static void fillDB()
	{
		helper.login("", "");	// Così viene utilizzato il LoginModule di test per l'inserimento degli utenti
		initStudents();
		initProfessors();
		helper.logout();
		initTeachings();
		initContents();
		initSubscriptions();

	}

	private static void initSubscriptions() 
	{
		helper.login("alessandro.montanar5@studio.unibo.it", "ale");
		Teaching t = searchBean.findTeachingByName(teaching1Name);
		studentManager.subscribeToTeaching(t);
		helper.logout();

		helper.login("lorena.qendro@studio.unibo.it", "lorena");
		studentManager.subscribeToTeaching(t);
		helper.logout();
	}

	private static void initTeachings()
	{
		helper.login("alessandro.montanar5@studio.unibo.it", "ale");
		User editor1 = searchBean.findProfessorsByName(editor1Name).get(0);
		Teaching teaching1 = new Teaching(6,1,"MAT","Scienza delle merendine",null);
		teaching1 = adminBean.addTeaching(teaching1, editor1.getMail(), null);
		teaching1 = new Teaching(6,1,"MAT","Scienza delle costruzioni",null);
		teaching1 = adminBean.addTeaching(teaching1, editor1.getMail(), null);
		teaching1 = new Teaching(6,1,"MAT","Statistica",null);
		teaching1 = adminBean.addTeaching(teaching1, editor1.getMail(), null);

		User editor2 = searchBean.findProfessorsByName(editor2Name).get(0);
		Teaching teaching2 = new Teaching(6,1,"MAT","Fon. Informatica",null);
		teaching2 = adminBean.addTeaching(teaching2, editor2.getMail(), null);
		teaching2 = new Teaching(6,1,"MAT","Ingegneria vs Matematica",null);
		teaching2 = adminBean.addTeaching(teaching2, editor2.getMail(), null);
		teaching2 = new Teaching(6,1,"MAT","Denti vs Trenitalia",null);
		teaching2 = adminBean.addTeaching(teaching2, editor2.getMail(), null);


		helper.logout();
	}

	private static void initContents() 
	{
		// Creo due insegnamenti
		helper.login("alessandro.montanar5@studio.unibo.it", "ale");
		User editor1 = searchBean.findProfessorsByName(editor1Name).get(0);
		User assistant1 = searchBean.findProfessorsByName(assistant1Name).get(0);
		Teaching teaching1 = new Teaching(6,1,"MAT",teaching1Name,null);
		String assistants[] = {assistant1.getMail()};
		teaching1 = adminBean.addTeaching(teaching1, editor1.getMail(), assistants);

		User editor2 = searchBean.findProfessorsByName(editor2Name).get(0);
		User assistant2 = searchBean.findProfessorsByName(assistant2Name).get(0);
		Teaching teaching2 = new Teaching(6,1,"MAT",teaching2Name,null);
		assistants[0] = assistant2.getMail();
		teaching2 = adminBean.addTeaching(teaching2, editor2.getMail(), assistants);
		helper.logout();

		// Popolo il primo insegnamento
		helper.login("silvano.martello@uunibo.it", "silvano");
		Category cat1 = new Category(category1Name,"", editor1);
		Content mat2 = new Material(material1Name,material1Path,"",editor1);
		cat1.appendContent(mat2);

		Category cat2 = new Category(category2Name,"", editor1);
		Content info = new Information(ContentType.INFORMATION,info1Name,"","",editor1);
		Content mat = new Material(material2Name,material1Path,"",editor1);
		cat2.appendContent(info);
		cat2.appendContent(mat);
		cat2.appendContent(cat1);

		// TODO va bene se ContentsRoot è EAGER
		profManager.appendContent(teaching1.getContentsRoot().getId(), cat2);

		// Categoria con un bel po di roba per vedere il caricamento da DB
		Category cat3 = new Category(category3Name,"", editor1);
		for(int i = 0; i < 200; i++)
		{
			cat3.appendContent(new Information(ContentType.INFORMATION, "Info"+i, "body of info"+i, "", editor1));
		}
		profManager.appendContent(teaching1.getContentsRoot().getId(), cat3);

		helper.logout();

		// Popolo il secondo insegnamento
		helper.login("enrico.denti@uunibo.it", "enrico");
		cat1 = new Category(category1NameBis,"", editor2);
		cat2 = new Category(category2NameBis,"", editor2);
		mat2 = new Material(material1NameBis,material1Path,"",editor2);
		info = new Information(ContentType.INFORMATION,info1NameBis,"","",editor2);
		mat = new Material(material2NameBis,material1Path,"",editor2);
		cat1.appendContent(info);
		cat1.appendContent(mat);
		cat1.appendContent(mat2);
		cat1.appendContent(cat2);

		// TODO va bene se ContentsRoot è EAGER
		profManager.appendContent(teaching2.getContentsRoot().getId(), cat1);
		helper.logout();

		//		totalContents = searchBean.getAllContents().size();
		//		totalCategories = searchBean.findContensByType(ContentType.CATEGORY).size();
		//		totalMaterials = searchBean.findContensByType(ContentType.MATERIAL).size();
		//		totalInformations = searchBean.findContensByType(ContentType.INFORMATION).size();
	}

	private static void initProfessors()
	{
		Role professor = new Role("professor");
		professor = adminBean.addRole(professor);
		
		User p1 = new User("silvano.martello@uunibo.it", "silvano", "Silvano", "Martello", new Role[]{professor});
		User p2 = new User("paolo.bellavista@uunibo.it","paolo","Paolo","Bellavista", new Role[]{professor});
		User p3 = new User("enrico.denti@uunibo.it", "enrico", "Enrico", "Denti", new Role[]{professor});
		User p4 = new User("giuseppe.bellavia@uunibo.it","giuseppe","Giuseppe","Bellavia", new Role[]{professor});

		p1 = adminBean.addUser(p1);
		p2 = adminBean.addUser(p2);
		p3 = adminBean.addUser(p3);
		p4 = adminBean.addUser(p4);
	}
	
	private static void initStudents()
	{
		Role student = new Role("student");
		Role admin = new Role("admin");
		admin = adminBean.addRole(admin);
		student = adminBean.addRole(student);
		
//		Role student = null;
//		Role admin = null;
//		List<Role> roles = searchBean.getAllRoles();
//		for(Role r : roles)
//		{
//			if(r.getRoleName().equalsIgnoreCase("student"))
//				student = r;
//			
//			if(r.getRoleName().equalsIgnoreCase("admin"))
//				admin = r;
//		}
		
		Subscriber me = new Subscriber("alessandro.montanar5@studio.unibo.it", "ale", "Alessandro", "Montanari", new Role[]{admin, student});
		Subscriber lorena = new Subscriber("lorena.qendro@studio.unibo.it", "lorena", "Lorena", "Qendro", new Role[]{student});
		User s1 = new User("mario.rossi@sstudio.unibo.it", "mario", "Mario", "Rossi", new Role[]{student});
		User s2 = new User("piero.bianchi@sstudio.unibo.it", "piero", "Piero", "Bianchi", new Role[]{student});
		User s3 = new User("luca.gialli@sstudio.unibo.it", "luca", "Luca", "Gialli", new Role[]{student});
		User s4 = new User("marco.verdi@sstudio.unibo.it", "marco", "Marco", "Verdi", new Role[]{student});

		User meUser = (User)me;
		meUser = adminBean.addUser(meUser);
		User lorenaUser = (User)lorena;
		lorenaUser = adminBean.addUser(lorenaUser);
		s1 = adminBean.addUser(s1);
		s2 = adminBean.addUser(s2);
		s3 = adminBean.addUser(s3);
		s4 = adminBean.addUser(s4);
	}

	protected static void cleanDB()
	{
		helper.login("alessandro.montanar5@studio.unibo.it", "ale");

		deleteStudents();
		deleteNotifications();
		deleteTeachings();
		deleteProfessors();
		deleteRoles();

		helper.logout();
	}

	private static void deleteRoles() 
	{
		//Elimino tutti i ruoli
		List<Role> roles = searchBean.getAllRoles();
		for(Role role : roles)
		{
			adminBean.removerRole(role);
		}
	}

	private static void deleteProfessors() 
	{
		// Elimino tutti i professori ma non i ruoli
		List<User> users = searchBean.getAllProfessors();
		for(User user : users)
		{
			adminBean.removeUser(user);
		}
	}

	private static void deleteStudents() 
	{
		// Elimino tutti gli studenti e di conseguenza tutte le sottoscrizioni ma non le notifiche e i ruoli
		List<User> users = searchBean.getAllStudents();
		for(User user : users)
		{
			adminBean.removeUser(user);
		}
	}

	private static void deleteTeachings()
	{
		// Elimino tutti gli insegnamenti e di conseguenza tutti i contenuti
		List<Teaching> teachings = searchBean.getAllTeachings();
		for (Teaching teaching : teachings) 
		{
			adminBean.removeTeachingById(teaching.getId());
		}
	}

	//	private static void deleteUsersAndRoles()
	//	{
	//		// Elimino tutti gli utenti e di conseguenza tutte le sottoscrizioni ma non le notifiche e i ruoli
	//		List<User> users = searchBean.getAllUsers();
	//		for(User user : users)
	//		{
	//			adminBean.removeUser(user);
	//		}
	//
	//		//Elimino tutti i ruoli
	//		List<Role> roles = searchBean.getAllRoles();
	//		for(Role role : roles)
	//		{
	//			adminBean.removerRole(role);
	//		}
	//	}

	private static void deleteNotifications()
	{
		// Elimino tutte le notifiche
		List<Notification> notifications = searchBean.getAllNotifications();
		for(Notification not : notifications)
		{
			adminBean.removeNotificationById(not.getId());
		}
	}

	public static void main(String[] args) 
	{
		TestsCommon.init();
		TestsCommon.cleanDB();
		TestsCommon.fillDB();
	}



}
