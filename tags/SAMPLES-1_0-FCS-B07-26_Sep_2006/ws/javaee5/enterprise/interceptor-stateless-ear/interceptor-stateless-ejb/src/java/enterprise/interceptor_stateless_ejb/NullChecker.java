/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */
package enterprise.interceptor_stateless_ejb;

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class NullChecker {

    @AroundInvoke
    public Object checkIfNull(InvocationContext ctx)
        throws Exception {
        Method method = ctx.getMethod();
        if (method.getName().equals("initUpperCase")) {
            String param = (String) (ctx.getParameters()[0]);
            if (param == null) {
                // An interceptor can throw any runtime exception or
                // application exceptions that are allowed in the
                // throws clause of the business method
                throw new BadArgumentException("Illegal argument: null");
            }
        }

        // Proceed to the next interceptor
        return ctx.proceed();
    }

}
