package it.unibo.myalma.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdministrationBeanTestCase.class,
				ProfessorManagerBeanTestCase.class, 
				SearchBeanTestCase.class })
public class AllTests {

}
