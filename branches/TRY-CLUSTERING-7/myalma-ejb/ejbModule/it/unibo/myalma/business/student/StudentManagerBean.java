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

@Stateless
@Local(IStudentManager.class)
//@Remote(it.unibo.myalma.business.remote.IStudentManagerRemote.class)
@RolesAllowed({"student"})
public class StudentManagerBean implements IStudentManager 
{
	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	protected SessionContext context;

	@Override
	public void subscribeToTeaching(Teaching teaching)
	{
		User student = entityManager.find(User.class, context.getCallerPrincipal().getName());

		if(!(student instanceof Subscriber))
			throw new IllegalArgumentException("The user " + student.getName() + " is not a subscriber");
		
		Subscriber subscriber = (Subscriber)student;

		// Uno studente non pu˜ sottoscriversi due volte allo stesso corso
		for(Subscription subscription : subscriber.getSubscriptions())
		{
			if(subscription.getTeaching().equals(teaching))
			{
				throw new IllegalStateException("The user " + subscriber.getName() + " is already subscribed to " + teaching.getName());
			}
		}

		Subscription subscription = new Subscription(teaching);

		subscriber.getSubscriptions().add(subscription);
	}

	@Override
	public void desubscribeFromTeaching(Teaching teaching) 
	{
		User student = entityManager.find(User.class, context.getCallerPrincipal().getName());

		if(!(student instanceof Subscriber))
			throw new IllegalArgumentException("The user " + student.getName() + " is not a subscriber");

		Subscriber subscriber = (Subscriber)student;

		for(Subscription subscription : subscriber.getSubscriptions())
		{
			if(subscription.getTeaching().equals(teaching))
			{
				subscriber.getSubscriptions().remove(subscription);
				break;
			}
		}
	}
}
