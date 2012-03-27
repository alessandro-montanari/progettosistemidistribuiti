package it.unibo.myalma.business;


public interface IUserHelper
{
	boolean isUserInRole(String rolename);
	String getMail();
	boolean isLogged();
//	String getType();
}
