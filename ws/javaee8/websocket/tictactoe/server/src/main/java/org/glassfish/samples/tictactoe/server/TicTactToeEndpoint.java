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
package org.glassfish.samples.tictactoe.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author johan
 */
@ServerEndpoint("/endpoint")
public class TicTactToeEndpoint {

    private static final List<Game> activeGames = new LinkedList<Game>();
    private static final Map<String, Session> sessions = new HashMap<String, Session>();
    private static final Logger LOGGER = Logger.getLogger("tictactoe");
    private static Game lastGame = new Game();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        LOGGER.info("Opening Endpoint for session " + session);

        synchronized (activeGames) {
            String sid = session.getId();
            sessions.put(sid, session);
            if (lastGame.getPlayer1() == null) {
                lastGame.setPlayer1(sid);
                session.getBasicRemote().sendText("p1");

            } else {
                lastGame.setPlayer2(sid);
                activeGames.add(lastGame);
                session.getBasicRemote().sendText("p3");
                String p1 = lastGame.getPlayer1();
                Session s1 = sessions.get(p1);
                System.out.println("second one");
                System.out.println("p1 = " + p1);
                System.out.println("sessions = " + sessions);
                System.out.println("s1 = " + s1);
                s1.getBasicRemote().sendText("p2");
                System.out.println("done sending message to first player");
                lastGame = new Game();

            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("CLOSED! " + session);
        LOGGER.info("reason: " + reason.getCloseCode() + "-- " + reason.getReasonPhrase());
        sessions.remove(session.getId());
    }

    @OnMessage
    public void onMessage(String obj, Session session) throws IOException {
        LOGGER.info("Got message from session " + session + ": " + obj);
        String sid = session.getId();
        for (Game candidate : activeGames) {

            if (sid.equals(candidate.getPlayer1())) {
                String other = candidate.getPlayer2();
                Session s2 = sessions.get(other);
                s2.getBasicRemote().sendText(obj);
            }
            if (sid.equals(candidate.getPlayer2())) {
                String other = candidate.getPlayer1();
                Session s1 = sessions.get(other);
                s1.getBasicRemote().sendText(obj);
            }
        }
    }
}
