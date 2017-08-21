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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ApplicationScoped;
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
public class StockTickerBean {

    @Inject @WebHandlerContext("/stockticker")
    private WebCommunicationContext wcc;
    private Timer timer = null;
    private Task task = null;
    private String symbols = "";
    private String openPrice = null;
    private Map <String, String>symbolOpenPrice = null;

    public StockTickerBean() {
    }

    public void getStockInfo() {
        synchronized(this) {
            if (symbolOpenPrice == null) {
                symbolOpenPrice = new HashMap<String, String>();
            }
            if (null == timer) {
                timer = new Timer();
                if (null == task) {
                    task = new Task();
                }
                timer.schedule(task, 0, 3 * 1000);
            }
        }
    }

    public void reset() {
        synchronized(this) {
            setSymbols("");
        }
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }
    
    // -- Message composition -------------------------------------------

    private String composeMessage() {
        String[] temp;
        temp = symbols.split(" ");
        StringBuilder sb = new StringBuilder();
        String openPrice = null;
        for (String t : temp) {
            String sym = t.trim();
            if (sym.length() > 0) {
                openPrice = symbolOpenPrice.get(sym);
                if (openPrice == null) {
                    openPrice = getTickerFromWebService(sym);
                    symbolOpenPrice.put(sym, openPrice);
                } 
                String price = getTickerFromWebService(sym);
                String chg = null;
                if (Double.valueOf(openPrice.trim()).doubleValue() < 
                    Double.valueOf(price.trim()).doubleValue()) {
                    chg = "up";
                } else if (Double.valueOf(openPrice.trim()).doubleValue() > 
                    Double.valueOf(price.trim()).doubleValue()) {
                    chg = "down";
                } else {
                    chg = "";
                }
                sb.append(sym).append(':').append(openPrice).
                    append(':').append(price).append(':').append(chg);
                sb.append(' ');
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    private String getTickerFromWebService(String symbol) {
        Random r = new Random();
        double q = symbol.hashCode() % 100 + r.nextFloat() * 5;
        return Double.toString(q).substring(0, 5);
    }
    
    class Task extends TimerTask {
        public void run() {
            for (WebCommunicationHandler wch : wcc.getHandlers()) {
                try {
                    String msg = composeMessage();
                    ((ServerSentEventHandler)wch).sendMessage(msg, "stock");
                } catch (IOException e) {
                    wch.close();
                }
            }
        }

    }
}
