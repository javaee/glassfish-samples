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
/*
 * Address.java
 *
 * Created on November 24, 2005, 7:02 PM
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ejb.persistence;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.*;


@Entity
//name defaults to the unqualified entity class name.        
//default access is property.
    
public class Address implements java.io.Serializable{
    
    private String addressID;
    private String street;
    private String city;
    private String zip;
    private String state;
    
    public Address(String id, String street, String city, String zip, String state){

        setAddressID(id);
        setStreet(street);
        setCity(city);
        setZip(zip);
        setState(state);

    }
    
    public Address(){
    
    }
    
    @Id //this is the default 
    @Column(name="addressID")
    public String getAddressID(){      //primary key
        return addressID;
    }
    public void setAddressID(String id){
        this.addressID=id;
    }
    
    //will default to Street
    public String getStreet(){
        return street;
    }
    public void setStreet(String street){
        this.street=street;
    }

    //will default to city
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }

    //will default to zip
    public String getZip(){
        return zip;
    }
    public void setZip(String zip){
        this.zip=zip;
    }

    //will default to state
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }
    
    
}
