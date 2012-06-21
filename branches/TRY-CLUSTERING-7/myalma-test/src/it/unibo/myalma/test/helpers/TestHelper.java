package it.unibo.myalma.test.helpers;

import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
import javax.naming.*; 
import java.security.Security; 
import java.util.*;
import org.jboss.sasl.JBossSaslProvider;
import java.util.HashMap;
import java.util.Hashtable;

import it.unibo.myalma.business.search.ISearch;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.AppCallbackHandler;
import org.jboss.security.client.SecurityClient;
import org.jboss.security.client.SecurityClientFactory;

public class TestHelper 
{
	static { Security.addProvider(new JBossSaslProvider()); }
	
	ISearch search = null;
	static Context ctx = null;
	SecurityClient ssclient = null;
	
	public TestHelper()
	{
		Hashtable<String, String> prop = new Hashtable<String, String>();
		prop.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
//		prop.put(Context.PROVIDER_URL, "remote://localhost:4447");
//		prop.put(Context.SECURITY_PRINCIPAL, "guest");
//		prop.put(Context.SECURITY_CREDENTIALS, "password");
		try {
			ctx = new InitialContext(prop);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void login(String user, String password) 
	{
		try {
			// Callback handler
			AppCallbackHandler apch = new AppCallbackHandler(user, password.toCharArray());

			// Setup the login configuration
			Configuration.setConfiguration(new LoginAuthConfiguration());

			// Create security client and login to the server
			ssclient = SecurityClientFactory.getSecurityClient();
			ssclient.setJAAS("client-module", apch);
			//		    ssclient.setVmwideAssociation(true);

			ssclient.login();
			

		} catch (LoginException ex) {
			System.err.println("Error login: " + ex.getMessage());
		} catch (Exception e) {
			System.err.println("Error creating the secyrity client.");
		}
	}
	
	public void logout()
	{
		ssclient.logout();
	}
	
	public Object lookup(String jndiName)
	{
		Object res = null;
		try {
			res = ctx.lookup(jndiName);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	public static class LoginAuthConfiguration extends Configuration 
	{

		/** The jBoss client login module. */
		private static final String JBOSS_CLIENT_MODULE = "org.jboss.security.ClientLoginModule";

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
			AppConfigurationEntry conf = new AppConfigurationEntry(JBOSS_CLIENT_MODULE, REQUIRED, new HashMap<String, Object>());
			return new AppConfigurationEntry[]{conf};
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void refresh() {
		}

	}

	
}
