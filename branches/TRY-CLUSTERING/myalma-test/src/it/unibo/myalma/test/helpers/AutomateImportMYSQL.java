package it.unibo.myalma.test.helpers;

import java.io.IOException;

public class AutomateImportMYSQL 
{
	public static void main(String[] args) 
	{
		AutomateImportMYSQL automator = new AutomateImportMYSQL();
		automator.importData("./sql-scripts/myalma-dump.sql");
	}
	
	public void importData(String filePath)
	{
		// Il nome utente e la password devono essere attaccate alle opzioni "-u" e "-p"
		// Eventualmente cambiare il path per "mysql" o scrivere solo "mysql" se il comando è già presente nel path di sistema
		String cmd[] = { "/usr/local/mysql/bin/mysql", "-usd10user", "-psd10pwd", "-e", "source " + filePath };
		Runtime run = Runtime.getRuntime() ;
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor() ;
		
			System.out.println("Import Completed!!");
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void cleanDB()
	{
		// Il nome utente e la password devono essere attaccate alle opzioni "-u" e "-p"
		// Eventualmente cambiare il path per "mysql" o scrivere solo "mysql" se il comando è già presente nel path di sistema
		String cmd[] = { "/usr/local/mysql/bin/mysql", "-usd10user", "-psd10pwd", "-e", "source ./sql-scripts/clean-DB.sql" };
		Runtime run = Runtime.getRuntime() ;
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor() ;
		
			System.out.println("Clean Completed!!");
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

}
