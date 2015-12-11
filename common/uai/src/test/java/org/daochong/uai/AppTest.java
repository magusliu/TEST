package org.daochong.uai;

import java.util.ArrayList;
import java.util.List;

import org.daochong.ucm.Configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
       List<Configuration> list = new ArrayList<Configuration>();
       Configuration config = new Configuration();
       config.setConfigId("A");
       config.setGroupId("DEFAULT");
       config.setConfigTime(System.currentTimeMillis());
       list.add(config);
       
       config = new Configuration();
       config.setConfigId("B");
       config.setGroupId("DEFAULT");
       config.setConfigTime(System.currentTimeMillis());
       System.out.println(list.contains(config));
    }
}
