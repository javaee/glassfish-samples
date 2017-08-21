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
package stockticker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;

/**
 * StockTickerBean class
 */
@Named
@ApplicationScoped
public class TimeBean {

    @Inject @WebHandlerContext("/stockticker")
    private WebCommunicationContext wcc;
    private Timer timer = null;
    private Task task = null;

    public TimeBean() {

    }

    private String time = "";
    public String getTime() {
        synchronized(this) {
            if (null == timer) {
                timer = new Timer();
                if (null == task) {
                    task = new Task();
                }
                timer.schedule(task, 0, 1 * 1000);
            }
        }
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    // -- Message composition -------------------------------------------

    private String composeMessage() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    class Task extends TimerTask {
        public void run() {
            for (WebCommunicationHandler wch : wcc.getHandlers()) {
                try {
                    String msg = composeMessage();
                    ((ServerSentEventHandler)wch).sendMessage(msg, "time");
                } catch (IOException e) {
                    wch.close();
                }
            }
        }

    }
}
