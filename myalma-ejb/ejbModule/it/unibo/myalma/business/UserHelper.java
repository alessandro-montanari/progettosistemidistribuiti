package it.unibo.myalma.business;


import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

@Name("userHelper")
@Stateless
@Remote(IUserHelper.class)
public class UserHelper implements IUserHelper
{
	@Resource
	SessionContext context;
	
	public String getMail()
	{
		return context.getCallerPrincipal().getName();
	}
	
	public boolean isLogged()
	{
		return context.getCallerPrincipal() != null;
	}

//	@Override
//	public String getType() 
//	{
//		return
//	}

	@Override
	public boolean isUserInRole(String roleName) 
	{
		return context.isCallerInRole(roleName);
	}

}
