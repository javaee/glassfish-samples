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

package enterprise.criteriaQuery.ejb;

import enterprise.criteriaQuery.entity.*;

import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

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
        CriteriaBuilder criteriaBuilder = em.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Order> cquery = criteriaBuilder.createQuery(Order.class);
        Root<Order> order = cquery.from(Order.class);
        MapJoin<Order, Item, LineItem> lineItemsMap = order.joinMap("lineItems");
        cquery.
                select(order).
                where(
                    criteriaBuilder.equal(
                            lineItemsMap.key().get("name"),criteriaBuilder.parameter(String.class, "name")
                            )
                    );
        TypedQuery<Order> tq = em.createQuery(cquery);
        tq.setParameter("name", itemName);

        List<Order> orders = tq.getResultList();
        for (Order o : orders) {
            outputStream.println("Retrieved Order id is: " + o.getId());
        }
    }

    private void queryDataForOrder(int orderId, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Map entries for Order Id : " + orderId);
        CriteriaBuilder criteriaBuilder = em.getEntityManagerFactory().getCriteriaBuilder();

        CriteriaQuery<Map.Entry> cquery = criteriaBuilder.createQuery(Map.Entry.class);

        Root<Order> order = cquery.from(Order.class);
        MapJoin<Order, Item, LineItem> lineItemsMap = order.joinMap("lineItems");
        cquery.
                select(lineItemsMap.entry()).
                where(
                    criteriaBuilder.equal(
                            order.get("id"),criteriaBuilder.parameter(Integer.class, "id")
                            )
                    );
        TypedQuery<Map.Entry> tq = em.createQuery(cquery);
        tq.setParameter("id", orderId);
        List<Map.Entry> entrySet = tq.getResultList();

        for (Map.Entry<Item, LineItem> itemLineItemEntry : entrySet) {
            Item item = itemLineItemEntry.getKey();
            LineItem lineItem  = itemLineItemEntry.getValue();
            outputStream.println("\tLineIten: " + lineItem.getLineItemNumber() + "\tItem: " + item.getName() + "\tQty Ordered: " + lineItem.getQuantity());
        }
    }

}
