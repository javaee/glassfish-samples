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
package enterprise.lottery_annotation_ejb_stateful;


import javax.ejb.EJB;
import javax.ejb.Stateful;

import enterprise.lottery_annotation_ejb_stateless.Date;


@Stateful
public class LotteryBean implements Lottery {

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date.today();
    }

    public void select(int number) {
        if( (number > -1) && (number < 10) ) {
            this.number = this.number + SPACE + 
                java.lang.Integer.toString(number);
	}
    }

    public void setName(String name) {
 	this.name = name;
    }


    //Dependency injection to get an instance of the Date EJB.
    @EJB(name="Date") 
    private Date date;

    private String name = "Super Lotto";
    private String number = "";
    private static final String SPACE = " "; 
}
