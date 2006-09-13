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
package enterprise.annotation_override_interceptor_ejb;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class ArgumentsChecker {

    @AroundInvoke
    public Object checkArgument(InvocationContext ctx) throws Exception {

        Map<String, Object> ctxData = ctx.getContextData();
        List<String> interceptorNameList = (List<String>)
            ctxData.get("interceptorNameList");
        if (interceptorNameList == null) {
            interceptorNameList = new ArrayList<String>();
            ctxData.put("interceptorNameList", interceptorNameList);
        }

        //Now add this interceptor name to the list
        interceptorNameList.add("ArgumentsChecker");

        Method method = ctx.getMethod();

        Object objParam  = ctx.getParameters()[0];
        if (! (objParam instanceof String)) {
            throw new BadArgumentException("Illegal argument type: " + objParam);
        }
        String param = (String) (ctx.getParameters()[0]);
        // Note that param cannot be null because
        // it has been validated by the previous (default) interceptor
        char c = param.charAt(0);
        if (!Character.isLetter(c)) {
            // An interceptor can throw any runtime exception or
            // application exceptions that are allowed in the
            // throws clause of the business method
            throw new BadArgumentException("Illegal argument: " + param);
        }

        // Proceed to call next interceptor OR business method
        return ctx.proceed();
    }

}
