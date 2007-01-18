/*
 * hello_jaxws_test.java
 *
 * Created on April 8, 2006, 11:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test;

import java.io.*;
import java.io.File;

/**
 *
 * @author bnevins
 * Note that it is not useful to use JUnit for this test because we are calling
 * appclient to run the client class.
 */
public class Hello_jaxws_test
{
    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("USAGE: hello_jaxws_test <javaee.home> <dir with client.Client>");
            System.exit(1);
        }
        
        String os = System.getProperty("os.name");
        boolean windows = os.indexOf("Windows") >= 0;
        String appclient = args[0] + "/bin/appclient";
        
        if(windows)
            appclient += ".bat";
        try
        {
            ProcessBuilder pb = new ProcessBuilder(appclient, "client.Client");
            pb.directory(new File(args[1]));
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String want = "Hello result = Hello " + System.getProperty("user.name") + "!";
            String got = br.readLine();

            System.out.println("Expected: [" + want + "]");
            System.out.println("Got: [" + got + "]");
            
            if(want.equals(got))
            {
                System.out.println("TEST SUCCEEDED");
                System.exit(0);
            }
            else
            {
                System.out.println("TEST FAILED:");
                System.exit(1);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
/**
 *
 * <target name="run-client" depends="compile-client,run-client-windows,run-client-unix"/>

    <target name="run-client-windows" if="windows">
		<exec executable="${javaee.home}/bin/appclient.bat" dir="${classesdir}">
			<arg value="client.Client"/>    
		</exec>    
    </target>

    <target name="run-client-unix" if="unix"> 
		<exec executable="${javaee.home}/bin/appclient" dir="${classesdir}" failifexecutionfails="false">
			<arg value="client.Client"/>    
		</exec>    
    </target>
 **/
