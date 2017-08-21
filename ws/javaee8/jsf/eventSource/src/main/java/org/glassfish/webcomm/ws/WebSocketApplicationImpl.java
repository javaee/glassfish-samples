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

package org.glassfish.webcomm.ws;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.webcomm.WebCommunicationClientImpl;
import org.glassfish.webcomm.WebCommunicationProviderImpl;
import org.glassfish.grizzly.websockets.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.glassfish.webcomm.api.MessageProcessor;
import org.glassfish.webcomm.api.WebCommunicationClient;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebSocketHandler;
import org.glassfish.webcomm.annotation.WebHandler;

/**
 * WebSocketApplicationImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 */
public class WebSocketApplicationImpl extends WebSocketApplication {

    private WebCommunicationProviderImpl provider;

    private Class<? extends WebSocketHandler> clazz;

    private Map<WebSocket, WebSocketHandler> socketToHandler;

    private Map<WebSocketHandler, WebSocket> handlerToSocket;

    private Map<String, WebCommunicationContext> contexts;

    private String path;

    private Class messageClass;

    public WebSocketApplicationImpl(WebCommunicationProviderImpl provider,
        Class<? extends WebSocketHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        this.provider = provider;
        this.clazz = clazz;
        this.contexts = contexts;
        this.path = path;
        socketToHandler = new ConcurrentHashMap<WebSocket, WebSocketHandler>();
        handlerToSocket = new ConcurrentHashMap<WebSocketHandler, WebSocket>();
    }

    public void closeHandler(WebSocketHandler wsh) {
        WebSocket ws = handlerToSocket.get(wsh);
        assert ws != null;
        ws.close();         // Calls onClose()
    }


    /*@Override
    public WebSocket createSocket(ProtocolHandler handler,WebSocketListener... listeners) throws IOException {
        WebSocket ws = new WebSocketImpl(listeners);
        try {
            // Create instance using CDI or by hand
            WebSocketHandler wsh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(wsh);

            // Update internal mappings
            socketToHandler.put(ws, wsh);
            handlerToSocket.put(wsh, ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ws;
    }*/


    @Override
    public WebSocket createSocket(ProtocolHandler protocolHandler,WebSocketListener... listeners) {
        WebSocket ws = new WebSocketImpl(protocolHandler,listeners);
        try {
            // Create instance using CDI or by hand
            WebSocketHandler wsh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(wsh);

            // Update internal mappings
            socketToHandler.put(ws, wsh);
            handlerToSocket.put(wsh, ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ws;
    }


    @Override
    public void onConnect(WebSocket ws) {
        WebSocketHandler wsh = socketToHandler.get(ws);
        assert wsh != null;
        // TODO: Populate WebCommunicationClient instance
        WebCommunicationClient dummy = new WebCommunicationClientImpl();
        wsh.onConnect(dummy);   // Handler callback
    }

    @Override
    public void onClose(WebSocket ws, DataFrame df) {
        // Get associated handler
        WebSocketHandler wsh = socketToHandler.get(ws);
        assert wsh != null;
        wsh.onClose();          // Handler callback

        // Release handler
        provider.releaseHandler(wsh);
        
        // Update context by removing handler
        WebCommunicationContext wcc = contexts.get(path);
        wcc.getHandlers().remove(wsh);

        // Update internal mappings
        handlerToSocket.remove(wsh);
        socketToHandler.remove(ws);
    }

    @Override
    public void onMessage(WebSocket socket, String data) {
        WebSocketHandler wsh = socketToHandler.get(socket);
        assert wsh != null;

        // Determine message class from annotation, if not done already
        if (messageClass == null) {
            WebHandler wh = clazz.getAnnotation(WebHandler.class);
            messageClass = wh.messageClass();
        }

        if (messageClass == String.class) {
            try {
                wsh.onMessage(data);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        } else {
            throw new UnsupportedOperationException("TODO");
            /*
            MessageProcessor messageProcessor = provider.getMessageProcessor(
                    messageClass);
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + messageClass);
            }
            Object obj = messageProcessor.read(new StringReader(data));
            wsh.onMessage(obj);    // Handler callback
            */
        }
    }

    @Override
    public void onMessage(WebSocket socket, byte[] data) {
        throw new UnsupportedOperationException("TODO");
        /*
        final String data = frame.getTextPayload();
        WebSocketHandler wsh = socketToHandler.get(socket);
        assert wsh != null;

        // Determine message class from annotation, if not done already
        if (messageClass == null) {
            WebHandler wh = clazz.getAnnotation(WebHandler.class);
            messageClass = wh.messageClass();
        }

        if (messageClass == String.class) {
            wsh.onMessage(data);
        } else {
            MessageProcessor messageProcessor = provider.getMessageProcessor(
                    messageClass);
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + messageClass);
            }
            Object obj = messageProcessor.read(new StringReader(data));
            wsh.onMessage(obj);    // Handler callback
        }
        */
    }

    public void sendMessage(WebSocketHandler wsh, String data) throws IOException {
        WebSocket ws = handlerToSocket.get(wsh);
        assert ws != null;
        ws.send(data);
    }


    @Override
    public boolean isApplicationRequest(HttpRequestPacket rqst) {
        // Include context path in matching, since two applications may have
        // the handlers at the same url-pattern
        String fullPath = provider.getServletContext().getContextPath()+path;
        return rqst.getRequestURI().equals(fullPath);
    }


}
