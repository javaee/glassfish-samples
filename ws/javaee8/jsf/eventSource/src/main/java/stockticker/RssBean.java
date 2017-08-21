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

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.util.*;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.inject.Inject;
import javax.inject.Named;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;

/**
 * RssBean class
 */
@Named
@ApplicationScoped
public class RssBean {

    @Inject @WebHandlerContext("/stockticker")
    private WebCommunicationContext wcc;
    private Timer timer = null;
    private Task task = null;
    private String symbols = "";

    public RssBean() {

    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public void getStockNews() {
        if (symbols.equals("")) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Application application = facesContext.getApplication();
            ValueBinding binding = application.createValueBinding("#{stockTickerBean}");
            StockTickerBean stockTickerBean = (StockTickerBean)binding.getValue(facesContext);
            this.symbols = stockTickerBean.getSymbols();
        }
        synchronized(this) {
            if (null == timer) {
                timer = new Timer();
                if (null == task) {
                    task = new Task();
                }
                timer.schedule(task, 0, 5 * 1000);
            }
        }
    }

    // -- Message composition -------------------------------------------

    private String composeMessage() {
        return getRSSFeeds(symbols);
    }

    private String getRSSFeeds(String symbols) {
        String rssURL = "http://finance.yahoo.com/rss/headline?s=";
        String feeds = "";
        String[] temp;
        temp = symbols.split(" ");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String t : temp) {
            String sym = t.trim();
            if (sym.length() > 0) {
                if (!first) {
                    rssURL += ",";
                }
                rssURL += sym;
                first = false;
            }
        }
        try {
            URL feedURL = new URL(rssURL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedURL));
            List list = feed.getEntries();
            if(list.size() > 0)    {
                Collections.shuffle(list);
                for (int i = 0; i < list.size(); i++ ) {
                    String container = "<p class=\"RSSlist\">";
                    sb.append(container);
                    String title = ((SyndEntry)list.get(i)).getTitle();
                    sb.append(title);
                    String endContainer = "</p>*****";
                    sb.append(endContainer);
                }
            }
        } catch(Exception e){}

        finally {
            return sb.toString();
        }
    }

    class Task extends TimerTask {
        public void run() {
            for (WebCommunicationHandler wch : wcc.getHandlers()) {
                try {
                    String msg = composeMessage();
System.err.println("SENDING MSG:"+msg);
                    ((ServerSentEventHandler)wch).sendMessage(msg, "rss");
                } catch (IOException e) {
                    wch.close();
                }
            }
        }

    }
}
