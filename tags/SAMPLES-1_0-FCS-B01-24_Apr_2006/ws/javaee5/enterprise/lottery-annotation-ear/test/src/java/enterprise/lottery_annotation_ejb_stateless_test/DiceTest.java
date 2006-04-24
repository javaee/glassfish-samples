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
package enterprise.lottery_annotation_ejb_stateless_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.lottery_annotation_ejb_stateless.Dice;

public class DiceTest {

    @Test public void Test1() {
	try {
            InitialContext ic = new InitialContext();
            Dice dice =
		(Dice) ic.lookup("enterprise.lottery_annotation_ejb_stateless.Dice");
  	    int number = dice.play();
            if((number < 0) || (number > 9)) {
		Assert.fail("Invalid number generated.");
            }
	} catch(Exception e) {
	    e.printStackTrace();
       	    Assert.fail("Encountered exception in DiceTest:Test1");
	}
    }

}
