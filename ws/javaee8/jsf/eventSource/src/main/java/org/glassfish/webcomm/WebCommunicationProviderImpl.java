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

package org.glassfish.webcomm;

import org.glassfish.webcomm.sse.ServerSentEventApplicationImpl;
import org.glassfish.webcomm.ws.WebSocketApplicationImpl;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;

import org.glassfish.webcomm.api.MessageProcessor;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.api.WebSocketHandler;
import org.glassfish.webcomm.spi.WebCommunicationProvider;

/**
 * WebCommunicationProviderImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Roger.Kitain@oracle.com
 */
@ApplicationScoped
public class WebCommunicationProviderImpl extends WebCommunicationProvider {

    private Map<Class<? extends WebSocketHandler>, WebSocketApplicationImpl>
            wsApps = new ConcurrentHashMap<Class<? extends WebSocketHandler>,
                                           WebSocketApplicationImpl>();

    private Map<Class<? extends ServerSentEventHandler>, ServerSentEventApplicationImpl>
            sseApps = new ConcurrentHashMap<Class<? extends ServerSentEventHandler>,
                                           ServerSentEventApplicationImpl>();

    private Map<Class, Class<? extends MessageProcessor>> messageProcessors;

    private Map<Class<? extends MessageProcessor>, MessageProcessor> processorCache;

    private ServletContext servletContext;

    @Override
    public WebCommunicationContext createWebCommunicationContext(String path) {
        return new WebCommunicationContextImpl(path);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void registerWSHandlerClass(Class<? extends WebSocketHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        WebSocketApplicationImpl wsa = wsApps.get(clazz);
        if (wsa == null) {
            wsa = new WebSocketApplicationImpl(this, clazz, contexts, path);
            WebSocketEngine.getEngine().register(wsa);
            wsApps.put(clazz, wsa);
        }
    }

    @Override
    public void registerSSEHandlerClass(Class<? extends ServerSentEventHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        ServerSentEventApplicationImpl ssea = sseApps.get(clazz);
        if (ssea == null) {
            ssea = new ServerSentEventApplicationImpl(this, clazz, contexts, path);
            sseApps.put(clazz, ssea);
        }
    }

    /**
     * Finds the SSE application instance that serves a URL. The end
     * of the URL is matched against the app's path.
     */
    public ServerSentEventApplicationImpl getSSEApplication(String url) {
        for (Map.Entry<Class<? extends ServerSentEventHandler>,
                       ServerSentEventApplicationImpl> e : sseApps.entrySet()) {
            if (url.endsWith(e.getValue().getPath())) {
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public void sendWSMessage(WebSocketHandler wsh, Object obj) throws IOException {
        WebSocketApplicationImpl wsa = wsApps.get(wsh.getClass());
        assert wsa != null;
        if (obj instanceof String) {
            wsa.sendMessage(wsh, (String) obj);
        } else {
            MessageProcessor messageProcessor = getMessageProcessor(obj.getClass());
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + obj.getClass());
            }
            StringWriter sw = new StringWriter();
            messageProcessor.write(obj, sw);
            wsa.sendMessage(wsh, sw.toString());
        }
    }

    @Override
    public void sendSSEMessage(ServerSentEventHandler sseh, Object obj)
            throws IOException
    {
        ServerSentEventApplicationImpl ssea = sseApps.get(sseh.getClass());
        assert ssea != null;
        if (obj instanceof String) {
            ssea.sendMessage(sseh, (String) obj);
        } else {
            MessageProcessor messageProcessor = getMessageProcessor(obj.getClass());
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + obj.getClass());
            }
            StringWriter sw = new StringWriter();
            messageProcessor.write(obj, sw);
            ssea.sendMessage(sseh, sw.toString());
        }
    }

    @Override
    public void sendSSEMessage(ServerSentEventHandler sseh, Object obj, String eventName)
            throws IOException
    {
        ServerSentEventApplicationImpl ssea = sseApps.get(sseh.getClass());
        assert ssea != null;
        if (obj instanceof String) {
            ssea.sendMessage(sseh, (String) obj, eventName);
        } else {
            MessageProcessor messageProcessor = getMessageProcessor(obj.getClass());
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + obj.getClass());
            }
            StringWriter sw = new StringWriter();
            messageProcessor.write(obj, sw);
            ssea.sendMessage(sseh, sw.toString());
        }
    }


    @Override
    public void closeWSHandler(WebSocketHandler wsh) {
        WebSocketApplicationImpl wsa = wsApps.get(wsh.getClass());
        assert wsa != null;
        wsa.closeHandler(wsh);
    }

    @Override
    public void closeSSEHandler(ServerSentEventHandler sseh) {
        ServerSentEventApplicationImpl ssea = sseApps.get(sseh.getClass());
        assert ssea != null;
        ssea.closeHandler(sseh);
    }

    @Override
    public void registerMessageProcessor(Class clazz,
            Class<? extends MessageProcessor> processor) {
        if (messageProcessors == null) {
            messageProcessors = new ConcurrentHashMap<Class,
                    Class<? extends MessageProcessor>>();
        }
        messageProcessors.put(clazz, processor);
    }

    public MessageProcessor getMessageProcessor(Class clazz) {
        MessageProcessor processor = null;
        Class<? extends MessageProcessor> processorClass =
                messageProcessors.get(clazz);

        // Check if a processor instance is in the cache
        if (processorCache == null) {
            processorCache = new ConcurrentHashMap<
                    Class<? extends MessageProcessor>, MessageProcessor>();
        } else {
            processor = processorCache.get(processorClass);
        }

        // If no processor, create and cache one
        if (processor == null) {
            try {
                processor = processorClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processorCache.put(processorClass, processor);
        }

        return processor;
    }

    public <T extends WebCommunicationHandler> T createHandler(Class clazz,
            Map<String, WebCommunicationContext> contexts)
    {
        T handler = null;
        BeanManager bm;
        try {
            InitialContext ic = new InitialContext();
            bm = (BeanManager)ic.lookup("java:comp/BeanManager");
        } catch(Exception e) {
            bm = getBeanManager();
        }

        // Check if we handler can be instantiated via CDI
        Iterator<Bean<?>> it = bm.getBeans(clazz).iterator();
        if (it.hasNext()) {
            Bean bean = it.next();
            handler = (T) bean.create(bm.createCreationalContext(bean));
        } else {
            // Instantiate by hand with limited injection
            try {
                handler = (T) clazz.newInstance();
                WebCommunicationInjector.getInstance().inject(handler, contexts);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        handler.setProvider(this);
        return handler;
    }

    public <T extends WebCommunicationHandler> void releaseHandler(T handler) {
        // TODO: Need to release handler if created via CDI
    }

}
