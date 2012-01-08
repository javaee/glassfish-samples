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

package enterprise.annotation_override_interceptor_ejb_test;

import java.util.List;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.annotation_override_interceptor_ejb.StatelessSession;
import enterprise.annotation_override_interceptor_ejb.BadArgumentException;

public class InterceptorStatelessXmlEjbTest {

    private static final String UPPER_LOWER_CASE_INTERCEPTOR_NAME_LIST =
	"NullChecker, ArgumentsChecker, StatelessSessionBean";

    private static final String IS_ODD_NUMBER_INTERCEPTOR_NAME_LIST =
	"NullChecker, StatelessSessionBean";

			
    @Test public void test1() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.annotation_override_interceptor_ejb.StatelessSession#"
                + "enterprise.annotation_override_interceptor_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            Assert.assertEquals("Failed to change case",
                sless.initUpperCase("duke"), "Duke");

	    List<String> interceptorNames = sless.getInterceptorNamesFor("initUpperCase");

	    String delimiter = "";
	    StringBuilder sbldr = new StringBuilder();
	    for (String interceptorName : interceptorNames) {
		sbldr.append(delimiter).append(interceptorName);
		delimiter = ", ";
	    }
	    Assert.assertEquals("Failed interceptor order for initUpperCase", 
		sbldr.toString(), UPPER_LOWER_CASE_INTERCEPTOR_NAME_LIST);
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test2() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.annotation_override_interceptor_ejb.StatelessSession#"
                + "enterprise.annotation_override_interceptor_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            Assert.assertEquals("Failed to change case",
                sless.initLowerCase("Duke"), "duke");

	    List<String> interceptorNames = sless.getInterceptorNamesFor("initLowerCase");

	    String delimiter = "";
	    StringBuilder sbldr = new StringBuilder();
	    for (String interceptorName : interceptorNames) {
		sbldr.append(delimiter).append(interceptorName);
		delimiter = ", ";
	    }
	    Assert.assertEquals("Failed interceptor order for initLowerCase", 
		sbldr.toString(), UPPER_LOWER_CASE_INTERCEPTOR_NAME_LIST);
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test3() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.annotation_override_interceptor_ejb.StatelessSession#"
                + "enterprise.annotation_override_interceptor_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            Assert.assertEquals("Failed to check isOddNumber",
                sless.isOddNumber(3), true);

	    List<String> interceptorNames = sless.getInterceptorNamesFor("isOddNumber");

	    String delimiter = "";
	    StringBuilder sbldr = new StringBuilder();
	    for (String interceptorName : interceptorNames) {
		sbldr.append(delimiter).append(interceptorName);
		delimiter = ", ";
	    }
	    Assert.assertEquals("Failed interceptor order for isOddNumber", 
		sbldr.toString(), IS_ODD_NUMBER_INTERCEPTOR_NAME_LIST);
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

}
