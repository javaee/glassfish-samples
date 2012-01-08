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
 * Subscription.java
 *
 * Created on November 25, 2005, 11:01 AM
 *
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ejb.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.*;


@Entity
@NamedQuery(name="findAllSubscriptions", query="select s from Subscription s")

public  class Subscription implements java.io.Serializable{

    private String title;
    private String type;
    private Collection<Customer> customers;
    
    public Subscription(){
        
    }
    
    @Id
    public String getTitle(){ //primary key
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    //access methods for cmr fields
    @ManyToMany(mappedBy="subscriptions")
    public Collection<Customer> getCustomers(){
        return customers;
    }
    public void setCustomers(Collection<Customer> customers){
        this.customers=customers;
    }

    public Subscription (
            String title,
            String type) {

        if (type.equals(SubscriptionType.MAGAZINE)) {
            _create(title,SubscriptionType.MAGAZINE);
        } 
        else if (type.equals(SubscriptionType.JOURNAL)) {
            _create(title,SubscriptionType.JOURNAL);
        } 
        else if (type.equals(SubscriptionType.NEWS_PAPER)) {
            _create(title,SubscriptionType.NEWS_PAPER);
        }
        else
            _create(title,SubscriptionType.OTHER);

    }
    

    private String _create (
            String title,
            String type)  {
        

        setTitle(title);
        setType(type);
        return title;
    }
    
}


    
