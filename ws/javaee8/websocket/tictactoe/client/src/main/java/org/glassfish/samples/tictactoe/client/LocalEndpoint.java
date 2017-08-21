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
package org.glassfish.samples.tictactoe.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author Johan
 */
public class LocalEndpoint extends Endpoint implements MessageHandler.Whole<String> {

    public static TicTacToeClient tictactoe;
    private Session session;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Endpoint opened, session = " + session + ", config = " + config);
        this.session = session;
        session.addMessageHandler(this);
        tictactoe.setEndpoint(this);
    }

    @Override
    public void onMessage(final String message) {
        Platform.runLater(new Runnable() {
            public void run() {

                System.out.println("GOT MESSAGE: " + message);
                if ("p1".equals(message)) {
                    tictactoe.setInfo("Waiting for a second player to join...");
                }
                if ("p2".equals(message)) {
                    tictactoe.setInfo("You play 'O'");
                    tictactoe.setSymbol(1);
                    tictactoe.setOtherSymbol(2);
                    tictactoe.myTurn(true);
                }
                if ("p3".equals(message)) {
                    tictactoe.setInfo("You play 'X'");
                    tictactoe.setSymbol(2);
                    tictactoe.setOtherSymbol(1);
                }
                if (message.startsWith("o")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }
                if (message.startsWith("x")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }

            }
        });

    }

    void myMove(int coords, int symbol) {
        String move = ((symbol == 1) ? "o" : "x") + coords;
        System.out.println("send this move: " + move);
        try {
            session.getBasicRemote().sendText(move);
        } catch (IOException ex) {
            Logger.getLogger(LocalEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void done() throws IOException {
        System.out.println("closing session");
        session.close();
        System.out.println("closed session");
    }
}
