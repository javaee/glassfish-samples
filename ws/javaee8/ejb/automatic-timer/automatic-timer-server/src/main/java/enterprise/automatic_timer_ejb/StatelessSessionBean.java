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


package enterprise.automatic_timer_ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.Schedule;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import enterprise.automatic_timer_ejb.persistence.LogRecord;

@Stateless
public class StatelessSessionBean implements StatelessSession {

    @PersistenceContext EntityManager em;

    @Schedule(second="*/3", minute="*", hour="*", info="Automatic Timer Test")
    public void test_automatic_timer(Timer t) {
        long count = (Long)em.createNamedQuery("LogRecord.countLoggedTimeouts").getSingleResult();

        System.out.println("Call # " + (count + 1));
        if (count > 10) {
            throw new IllegalStateException("Too many timeouts received: " + count);
        } else if (count == 10) {
            LogRecord lr = new LogRecord("Canceling timer " + t.getInfo() + " at " + new Date());
            em.persist(lr);
            t.cancel();
            System.out.println("Done");
        } else {
            LogRecord lr = new LogRecord("" + t.getInfo() + " timeout received at " + new Date());
            em.persist(lr);
        }
    }

    public List<String> getRecords() {
        return (List<String>)em.createNamedQuery("LogRecord.findAllRecords").getResultList();
    }
}
