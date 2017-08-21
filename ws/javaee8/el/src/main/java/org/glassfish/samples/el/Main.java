/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package org.glassfish.samples.el;

import javax.el.*;

/**
 * A Sample for Expression Language 3.0
 * @author Kin-man Chung
 */
public class Main {

    ELProcessor elp = new ELProcessor();

    public static void main(String[] args) {
        Main test = new Main();
        test.run();
    }

    void run() {
        // obligatory test
        eval("'Hello, world!'");
        // static field reference
        eval("Boolean.TRUE");
        // static method reference
        eval("Integer.numberOfTrailingZeros(16)");
        // string concatenation
        eval("10 += 11");
        // assignment and semicolon operator
        eval("xx = 100; yy = 11; xx+yy");
        // lambda expression: immediate invokation
        eval("(x->x+1)(10)");
        // lambda expression: set to variable and recursive invokation
        eval("fact = n->n==0? 1: n*fact(n-1); fact(5)");
        // List construction
        eval("['eenie', 'meenie', 'miney', 'moe']");
        // Map construction
        eval("{'one':10, 'two':20, 'three':300}");
        // Simple collection operation
        eval("[1,2,3,4,5,6,7,8].stream().filter(i->i%2 == 0) \n" +
             "                          .map(i->i*10) \n" +
             "                          .toList()");
        // sort a list in descending order
        eval("[1,5,3,7,4,2,8].stream().sorted((i,j)->j-i).toList()");
    }

    void eval(String input) {
        System.out.println("\n====");
        System.out.println("Input EL string: " + input);
        Object ret = elp.eval(input);
        System.out.println("Output Value: " + ret);
    }
}
