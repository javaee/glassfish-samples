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

package org.glassfish.webcomm.sse;

import org.glassfish.webcomm.WebCommunicationClientImpl;
import org.glassfish.webcomm.WebCommunicationInjector;
import org.glassfish.webcomm.WebCommunicationProviderImpl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;

/**
 * ServerSentEventApplicationImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Roger.Kitain@oracle.com
 */
public class ServerSentEventApplicationImpl implements AsyncListener {

    private WebCommunicationProviderImpl provider;

    private Class<? extends ServerSentEventHandler> clazz;

    private Map<String, WebCommunicationContext> contexts;

    private Map<ServerSentEventHandler, AsyncContext> handlerToContext;

    private Map<AsyncContext, ServerSentEventHandler> contextToHandler;

    private String path;

    public ServerSentEventApplicationImpl(WebCommunicationProviderImpl provider,
        Class<? extends ServerSentEventHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        this.provider = provider;
        this.clazz = clazz;
        this.contexts = contexts;
        this.path = path;
        handlerToContext = new ConcurrentHashMap<ServerSentEventHandler, AsyncContext>();
        contextToHandler = new ConcurrentHashMap<AsyncContext, ServerSentEventHandler>();
    }

    public String getPath() {
        return path;
    }

    public void createConnection(AsyncContext asyncContext) {
        try {
            // Create instance using CDI or by hand
            ServerSentEventHandler sseh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(sseh);

            // Update internal mapping
            handlerToContext.put(sseh, asyncContext);
            contextToHandler.put(asyncContext, sseh);

            // Call onConnect on handler
            onConnect(sseh);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onConnect(ServerSentEventHandler sseh) {
        AsyncContext ac = handlerToContext.get(sseh);
        WebCommunicationClientImpl client = new WebCommunicationClientImpl();
        final ServletRequest r = ac.getRequest();
        client.setRemoteAddr(r.getRemoteAddr());
        client.setRemoteHost(r.getRemoteHost());
        client.setRemotePort(r.getRemotePort());
        client.setRemoteUser(((HttpServletRequest)r).getRemoteUser());
        client.setUserPricipal(((HttpServletRequest)r).getUserPrincipal());
        HttpServletRequest httpReq = (HttpServletRequest) ac.getRequest();
        client.setHttpSession(httpReq.getSession());
        sseh.onConnect(client);     // Handler callback
    }

    private static final byte[] dataKey;
    static {
        try {
            dataKey = "data:".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final byte[] eventKey;
    static {
        try {
            eventKey = "event:".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(ServerSentEventHandler sseh, String data)
            throws IOException
    {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }

            try {
                // Write message on response and flush
                HttpServletResponse res = (HttpServletResponse) ac.getResponse();
                ServletOutputStream sos = res.getOutputStream();
                sos.write(dataKey);
                sos.write(data.getBytes("UTF-8"));
                sos.write('\n');
                sos.write('\n');
                sos.flush();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } 
        }
    }

    public void sendMessage(ServerSentEventHandler sseh, String data, String eventName)
            throws IOException
    {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }

            try {
                // Write message on response and flush
                HttpServletResponse res = (HttpServletResponse) ac.getResponse();
                ServletOutputStream sos = res.getOutputStream();
                sos.write(eventKey);
                sos.write(eventName.getBytes("UTF-8"));
                sos.write('\n');
                sos.write(dataKey);
                sos.write(data.getBytes("UTF-8"));
                sos.write('\n');
                sos.write('\n');
                sos.flush();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeHandler(ServerSentEventHandler sseh) {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }
            ac.complete();      // calls onComplete()
        }
    }

    // -- AsyncListener --------------------------------------------------

    public void onComplete(AsyncEvent event) throws IOException {
        // Call onClose() on handler
        AsyncContext ac = event.getAsyncContext();
        ServerSentEventHandler sseh = contextToHandler.get(ac);
        sseh.onClose();         // Handler callback

        // Remove handler from context
        WebCommunicationContext wcc = contexts.get(path);
        wcc.getHandlers().remove(sseh);

        // Update internal mappings
        handlerToContext.remove(sseh);
        contextToHandler.remove(ac);

        // Release handler
        provider.releaseHandler(sseh);
    }

    public void onTimeout(AsyncEvent event) throws IOException {
        // no-op
    }

    public void onError(AsyncEvent event) throws IOException {
        // no-op
    }

    public void onStartAsync(AsyncEvent event) throws IOException {
        // no-op
    }

}
