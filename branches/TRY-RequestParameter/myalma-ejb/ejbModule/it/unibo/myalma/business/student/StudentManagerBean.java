package it.unibo.myalma.business.student;

import it.unibo.myalma.model.Subscription;
import it.unibo.myalma.model.Teaching;
import it.unibo.myalma.model.Subscriber;
import it.unibo.myalma.model.User;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Local;

/**
 * Session Bean implementation class StudentManagerBean
 */
@Stateless
@Local(IStudentManager.class)
@Remote(it.unibo.myalma.business.remote.IStudentManagerRemote.class)
public class StudentManagerBean implements IStudentManager 
{
	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	SessionContext context;

	/**
	 * Default constructor. 
	 */
	public StudentManagerBean() {

	}

	@Override
	@RolesAllowed({"student"})
	public void subscribeToTeaching(Teaching teaching)
	{
		User student = entityManager.find(User.class, context.getCallerPrincipal().getName());

		if(!(student instanceof Subscriber))
			throw new IllegalStateException("The user " + student.getName() + " is not a subscriber");
		
		Subscriber subscriber = (Subscriber)student;

		/** Precondizioni (Business Logic)**/
		// Uno studente non pu˜ sottoscriversi due volte allo stesso corso
		for(Subscription subscription : subscriber.getSubscriptions())
		{
			if(subscription.getTeaching().equals(teaching))
			{
				throw new IllegalStateException("The user " + subscriber.getName() + " is already subscribed to " + teaching.getName());
			}
		}

		Subscription subscription = new Subscription(teaching);
//		setDefaultSubscriptionPolicies(subscription);

		subscriber.getSubscriptions().add(subscription);

		entityManager.merge(student);
	}

//	private void setDefaultSubscriptionPolicies(Subscription subscription) 
//	{
//		NotificationPolicy policy = new NotificationPolicy("mail");
//
//		policy.getSettings().put(ContentType.INFORMATION + "_" + TypeOfChange.CHANGE, true);
//		policy.getSettings().put(ContentType.INFORMATION + "_" + TypeOfChange.INSERT, true);
//		policy.getSettings().put(ContentType.INFORMATION + "_" + TypeOfChange.REMOVE, true);
//		policy.getSettings().put(ContentType.NOTICE + "_" + TypeOfChange.CHANGE, true);
//		policy.getSettings().put(ContentType.NOTICE + "_" + TypeOfChange.INSERT, true);
//		policy.getSettings().put(ContentType.NOTICE + "_" + TypeOfChange.REMOVE, true);
//		policy.getSettings().put(ContentType.MATERIAL + "_" + TypeOfChange.CHANGE, true);
//		policy.getSettings().put(ContentType.MATERIAL + "_" + TypeOfChange.INSERT, true);
//		policy.getSettings().put(ContentType.MATERIAL + "_" + TypeOfChange.REMOVE, true);
//
//		subscription.getPolicies().add(policy);
//	}

	@Override
	@RolesAllowed({"student"})
	public void desubscribeFromTeaching(Teaching teaching) 
	{
		User student = entityManager.find(User.class, context.getCallerPrincipal().getName());

		/** Precondizioni **/
		if(!(student instanceof Subscriber))
			throw new IllegalStateException("The user " + student.getName() + " is not a subscriber");

		Subscriber subscriber = (Subscriber)student;

		for(Subscription subscription : subscriber.getSubscriptions())
		{
			if(subscription.getTeaching().equals(teaching))
			{
				subscriber.getSubscriptions().remove(subscription);
				break;
			}
		}

		entityManager.merge(student);
	}

}
