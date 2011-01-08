/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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
