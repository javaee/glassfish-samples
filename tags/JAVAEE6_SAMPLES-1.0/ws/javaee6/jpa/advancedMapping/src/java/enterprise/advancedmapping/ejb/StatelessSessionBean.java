/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
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

package enterprise.advancedmapping.ejb;

import enterprise.advancedmapping.entity.*;

import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

@Stateless
/**
 * Contains methods to create and query data
 */
public class StatelessSessionBean {

    @PersistenceContext
    private EntityManager em;

    public void createData(ServletOutputStream outputStream) {
        final int NO_OF_ITEMS = 10;

        //Create Items
        for(int i = 1 ; i <= NO_OF_ITEMS ; i++) {
            Item item = new Item(i, "ItemName" + i, i * 10);
            em.persist(item);
        }
        final int NO_OF_ORDERS = 1;
        for(int i = 1 ; i <= NO_OF_ORDERS ; i++) {
            createOrder(i);
        }
    }

    private void createOrder(int orderNumber) {
        Order o  = new Order("customer" + orderNumber);
        for(int lineItemNumber = 1 ; lineItemNumber <= 3 ; lineItemNumber++) {
            LineItem li = new LineItem(lineItemNumber, o,  lineItemNumber * orderNumber);
            o.addLineItem(em.find(Item.class, lineItemNumber), li);
        }
        em.persist(o);
    }

    public void queryData(ServletOutputStream outputStream) throws IOException {
        queryForOrderContainingItem("ItemName1", outputStream);
        queryDataForOrder(1, outputStream);

    }

    private void queryForOrderContainingItem(String itemName, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Order containing item : " + itemName);
        String query = "select o from Order o join o.lineItems li where KEY(li).name = ?1";
        Query q = em.createQuery(query);
        q.setParameter(1, itemName);
        outputStream.println("Executing query..." + query);
        List<Order> orders = q.getResultList();
        for (Order order : orders) {
            outputStream.println("Retrieved Order id is: " + order.getId());
        }
    }

    private void queryDataForOrder(int orderId, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Map entries for Order Id : " + orderId);
        String query = "select ENTRY(li) from Order o join o.lineItems li where o.id = ?1";
        Query q = em.createQuery(query);
        q.setParameter(1, orderId);
        outputStream.println("Executing query..." + query);
        List<Map.Entry<Item, LineItem>> entrySet = q.getResultList();
        for (Map.Entry<Item, LineItem> itemLineItemEntry : entrySet) {
            Item item = itemLineItemEntry.getKey();
            LineItem lineItem  = itemLineItemEntry.getValue();
            outputStream.println("\tLineIten: " + lineItem.getLineItemNumber() + "\tItem: " + item.getName() + "\tQty Ordered: " + lineItem.getQuantity());
        }
    }

}
