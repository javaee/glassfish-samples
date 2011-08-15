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

package enterprise.locking.ejb;

import enterprise.locking.entity.Part;
import enterprise.locking.entity.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Random;

@Stateless
public class StatelessSessionBean {

    @PersistenceContext
    private EntityManager em;

    private String name = "foo";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Initializes data for given number of consumers, suppliers and parts
     */
    public void initData(int numberOfConsumers, int numberOfSuppliers, int numberOfParts) {
        // user info
        int uid = 0;
        String fName = null;
        String lName = null;
        User u = null;
        int pid = 0;
        String pName = null;
        Part p = null;
        Part partRef[] = new Part[numberOfParts];

        for (int i = 0; i < numberOfParts; i++) {
            pid++;
            pName = "p" + pid;
            p = new Part(pid, pName);
            p.setAmount(500);
            partRef[i] = p;
            em.persist(p);
        }


        int partIndex = 1;
        Random randomGenerator = new Random();
        for (int i = 0; i < numberOfConsumers; i++) {
            uid++;
            fName = "f" + uid;
            lName = "l" + uid;
            u = new User(uid, fName, lName);
            u.setUserType(User.UserType.CONSUMER);
            u.setCount(2);
            // randam int from 0, ..., NT-1
            partIndex = randomGenerator.nextInt(numberOfParts);
            u.setPart(partRef[partIndex]);
            em.persist(u);
        }

        for (int i = 0; i < numberOfSuppliers; i++) {
            uid++;
            fName = "f" + uid;
            lName = "l" + uid;
            u = new User(uid, fName, lName);
            u.setUserType(User.UserType.SUPPLIER);
            u.setCount(10);
            partIndex = randomGenerator.nextInt(numberOfParts);
            u.setPart(partRef[partIndex]);
            em.persist(u);
        }
    }

    /**
     * A find followed by some think time, followed by update.
     */
    public boolean updateWithOptimisticLock(int uID, int s) {
        boolean updateSuccessfull = true;
        User u = em.find(User.class, uID);
        int pID = u.getPart().getId();
        Part p = em.find(Part.class, pID);

        // Simulate think time to allow parallel threads to find Usrs in parallel.
        simulateThinkTimeForSecond(s);
        int uCount = u.getCount();
        int pAmount = p.getAmount();
        // update part
        if (u.getUserType() == User.UserType.CONSUMER) {
            p.setAmount(pAmount - uCount);
        } else {
            p.setAmount(pAmount + uCount);
        }

        try {
            em.flush();
        } catch (OptimisticLockException e) {
            System.out.println("Got OptimisticLockException while updating with Optimistic Lock. " +
                    "The transaction will be rolled back");
            updateSuccessfull = false;
        } catch (PersistenceException e ) {
            System.out.println("Got Exception while updating with optimstic lock" + e);
            updateSuccessfull = false;
        }
        return updateSuccessfull;
    }

    /**
     * A find with pessimistic lock followed by some think time, followed by update.
     */
    public boolean updateWithPessimisticLock(int uID, int s) {
        boolean updateSuccessfull = true;

        User u = em.find(User.class, uID);
        int pID = u.getPart().getId();
        // Using Pessimistic lock to find the part object.
        Part p = em.find(Part.class, pID, LockModeType.PESSIMISTIC_WRITE);

        // Simulate think time to allow parallel threads to find in parallel.
        simulateThinkTimeForSecond(s);
        int uCount = u.getCount();
        int pAmount = p.getAmount();
        // update part
        if (u.getUserType() == User.UserType.CONSUMER) {
            p.setAmount(pAmount - uCount);
        } else {
            p.setAmount(pAmount + uCount);
        }

        try {
            em.flush();
        } catch (PersistenceException e) {
            updateSuccessfull = false;
        }
        return updateSuccessfull;

    }

    public void simulateThinkTimeForSecond(int sec) {
        //TODO check if sleep is allowed by EE spec
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception ex) {
            System.out.println("get exp in sleep");
        }
    }

}
