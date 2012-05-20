package it.unibo.myalma.business;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

@Name("userHelper")
@Stateless
@Local(IUserHelper.class)
public class UserHelperBean implements IUserHelper
{
	@Resource
	SessionContext context;
	
	@Override
	public String getMail()
	{
		return context.getCallerPrincipal().getName();
	}
	
	@Override
	public boolean isLogged()
	{
		return context.getCallerPrincipal() != null;
	}
}
