/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
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
package enterprise.messageboard.resources;

import enterprise.messageboard.entities.Message;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Singleton;

@Singleton
public class MessageHolderSingletonBean {

    private LinkedList<Message> list = new LinkedList<Message>();
    private int maxMessages = 10;

    int currentId = 0;

    public MessageHolderSingletonBean() {
        // initial content
        addMessage("msg0", new Date(0));
        addMessage("msg1", new Date(1000));
        addMessage("msg2", new Date(2000));
    }

    public List<Message> getMessages() {
        List<Message> l = new LinkedList<Message>();

        int index = 0;

        while(index < list.size() && index < maxMessages) {
            l.add(list.get(index));
            index++;
        }

        return l;
    }

    private synchronized int getNewId() {
        return currentId++;
    }
    

    public synchronized Message addMessage(String msg) {
        return addMessage(msg, new Date());
    }

    private synchronized Message addMessage(String msg, Date date) {
        Message m = new Message(date, msg, getNewId());

        list.addFirst(m);

        return m;
    }
    
    public Message getMessage(int uniqueId) {
        int index = 0;
        Message m;

        while(index < list.size()) {
            if((m = list.get(index)).getUniqueId() == uniqueId)
                return m;
            index++;
        }
        
        return null;
    }

    public synchronized boolean deleteMessage(int uniqueId) {
        int index = 0;

        while(index < list.size()) {
            if(list.get(index).getUniqueId() == uniqueId) {
                list.remove(index);
                return true;
            }
            index++;
        }

        return false;
    }
}
