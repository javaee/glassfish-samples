/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package org.glassfish.samples.websocket.auction;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.samples.websocket.auction.decoders.AuctionMessageDecoder;
import org.glassfish.samples.websocket.auction.encoders.AuctionMessageEncoder;
import org.glassfish.samples.websocket.auction.message.AuctionMessage;

/**
 * Runs multiple auctions.
 *
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
@ServerEndpoint(value = "/auction",
        decoders = {
                AuctionMessageDecoder.class,
        },
        encoders = {
                AuctionMessageEncoder.class
        }
)
public class AuctionServer {

    /*
     * Set of auctions (finished, running, to be started auctions).
     */
    private static final Set<Auction> auctions = Collections.unmodifiableSet(new HashSet<Auction>() {{
        add(new Auction(new AuctionItem("Swatch", "Nice Swatch watches, hand made", 100, 20)));
        add(new Auction(new AuctionItem("Rolex", "Nice Rolex watches, hand made", 200, 20)));
        add(new Auction(new AuctionItem("Omega", "Nice Omega watches, hand made", 300, 20)));
    }});

    @OnClose
    public void handleClosedConnection(Session session) {
        for (Auction auction : auctions) {
            auction.removeArc(session);
        }
    }

    @OnMessage
    public void handleMessage(AuctionMessage message, Session session){
        String communicationId;

        switch (message.getType()){
            case AuctionMessage.LOGOUT_REQUEST:
                handleClosedConnection(session);
                break;
            case AuctionMessage.AUCTION_LIST_REQUEST:
                StringBuilder sb = new StringBuilder("-");

                for (Auction auction : auctions) {
                    sb.append(auction.getId()).append("-").append(auction.getItem().getName()).append("-");
                }

                try {
                    session.getBasicRemote().sendObject((new AuctionMessage.AuctionListResponseMessage("0", sb.toString())));
                } catch (IOException | EncodeException e) {
                    Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, e);
                }
                break;
            case AuctionMessage.LOGIN_REQUEST:
                communicationId = message.getCommunicationId();
                for (Auction auction : auctions) {
                    if (communicationId.equals(auction.getId())) {
                        auction.handleLoginRequest(message, session);
                    }
                }
                break;
            case AuctionMessage.BID_REQUEST:
                communicationId = message.getCommunicationId();
                for (Auction auction : auctions) {
                    if (communicationId.equals(auction.getId())) {
                        auction.handleBidRequest(message, session);
                        break;
                    }
                }
                break;
        }

    }
}