/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package enterprise.annotation_override_interceptor_ejb;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

// This bean uses two interceptors to validate
// the input to its (only) business method.
// Note that a single interceptor would suffice
// but to demonstrate the use of interceptor
// chaining, we use two interceptors

@Stateless
@Interceptors({ArgumentsChecker.class})
public class StatelessSessionBean
    implements StatelessSession {

    private static final String KEY = "interceptorNameList";

    private static Map<String, List<String>> interceptorNamesForMethod
            = new HashMap<String, List<String>>();

    //The bean interceptor method just caches the interceptor names
    //  in a map which is queried in the getInterceptorNamesFor() method
    @AroundInvoke
    private Object intercept(InvocationContext invCtx)
    	throws Exception {
	System.out.println("Entered aroundInvoke for Bean");
        Map<String, Object> ctxData = invCtx.getContextData();
        List<String> interceptorNameList = (List<String>) ctxData.get(KEY);
        if (interceptorNameList == null) {
            interceptorNameList = new ArrayList<String>();
            ctxData.put(KEY, interceptorNameList);
        }

        //Add this interceptor also to the list of interceptors invoked!!
        interceptorNameList.add("StatelessSessionBean");
        
        //Cache the interceptor name list in a map that can be queried later
        String methodName = invCtx.getMethod().getName();
	synchronized (interceptorNamesForMethod) {
            interceptorNamesForMethod.put(methodName, interceptorNameList);
	}

	return invCtx.proceed();
    }

    // This business method is called after the interceptor methods.
    // Hence it is guaranteed that the argument to this method is not null
    // and it starts with a letter
    public String initUpperCase(String val) {
        String first = val.substring(0, 1);
        return first.toUpperCase() + val.substring(1);
    }

    // This business method is called after the interceptor methods.
    // Hence it is guaranteed that the argument to this method is not null
    // and it starts with a letter
    public String initLowerCase(String val) {
        String first = val.substring(0, 1);
        return first.toLowerCase() + val.substring(1);
    }

    //Note:-
    //  Since this method takes a int as a parameter, the ArgumentChecker
    //  inteceptor is disabled in the ejb-jar.xml for this method.
    public boolean isOddNumber(int val) {
        return ((val % 2) != 0);
    }

    /**
     * Only the default interceptor is used to intercept this method
     */
    public List<String> getInterceptorNamesFor(String methodName) {
        return interceptorNamesForMethod.get(methodName);
    }

}
