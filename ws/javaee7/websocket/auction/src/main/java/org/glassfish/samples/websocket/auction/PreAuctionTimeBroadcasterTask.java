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
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.glassfish.samples.websocket.auction.message.AuctionMessage;

/**
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
class PreAuctionTimeBroadcasterTask extends TimerTask {

    private final long startTimeMs;
    private final Auction owner;
    private static final long MILLISECONDS_IN_SECOND = 1000;
    private static final long MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * 60;
    private static final long MILLISECONDS_IN_HOUR = MILLISECONDS_IN_MINUTE * 60;
    private static final long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * 24;

    public PreAuctionTimeBroadcasterTask(Auction owner, long startTimeMs) {
        this.owner = owner;
        this.startTimeMs = startTimeMs;
    }

    @Override
    public void run() {
        long diff = startTimeMs - System.currentTimeMillis();

        if (diff < 0) {
            owner.switchStateToAuctionRunning();
        } else {
            if (!owner.getRemoteClients().isEmpty()) {
                String diffString = convertTimeDiffToString(diff);
                AuctionMessage.PreAuctionTimeBroadcastMessage tbm = new AuctionMessage.PreAuctionTimeBroadcastMessage("0", diffString);

                for (Session arc : owner.getRemoteClients()) {
                    try {
                        arc.getRemote().sendObject(tbm);
                    } catch (IOException | EncodeException e) {
                        Logger.getLogger(PreAuctionTimeBroadcasterTask.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        }
    }

    private static String convertTimeDiffToString(long timeDiff) {

        long days = timeDiff / MILLISECONDS_IN_DAY;
        long rest = timeDiff % MILLISECONDS_IN_DAY;

        long hours = rest / MILLISECONDS_IN_HOUR;
        rest = rest % MILLISECONDS_IN_HOUR;

        long minutes = rest / MILLISECONDS_IN_MINUTE;
        rest = rest % MILLISECONDS_IN_MINUTE;

        long seconds = rest / MILLISECONDS_IN_SECOND;
        return "" + days + ":" + hours + ":" + minutes + ":" + seconds;
    }
}
