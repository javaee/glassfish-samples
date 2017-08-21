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
package org.glassfish.samples.rest.chat.resources;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import javax.ejb.Singleton;


/**
 * Resource that handles one way asynchronous chatting based on consumes produces mechanism.
 * <p/>
 * Message is sent (produces) using POST http method handled by {@link #postMessage(javax.ws.rs.container.AsyncResponse, String)}
 * and this message is received (consumed) by GET http method handled by {@link #getMessage(javax.ws.rs.container.AsyncResponse,
 * String)}.
 * <p/>
 * This implementation of produces consumes mechanism does not keep a queue of messages but a queue of responses waiting for
 * messages. Message is always delivered directly from thread handling posting of message to the thread handling receiving of
 * message. Request processing must be therefore synchronized by blocking threads. In order to save server resources the original
 * threads are returned to the container.
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
@Path("/")
@Singleton
public class ChatResource {

    /**
     * This queue synchronizes produces/consumes operations. It contains {@link AsyncResponse async responses} into
     * which post message should be written.
     */
    private final BlockingQueue<AsyncResponseWrapper> suspended = new ArrayBlockingQueue<AsyncResponseWrapper>(10);

    /**
     * Internal response wrapper which bundles response with id.
     */
    private static class AsyncResponseWrapper {
        private final AsyncResponse asyncResponse;
        private final String id;

        private AsyncResponseWrapper(AsyncResponse asyncResponse, String id) {
            this.asyncResponse = asyncResponse;
            this.id = id;
        }

        public AsyncResponse getAsyncResponse() {
            return asyncResponse;
        }

        public String getId() {
            return id;
        }
    }


    /**
     * Handle a HTTP get message asynchronously (suspend response in order to release the container thread instead of
     * blocking it on the blocking queue).
     *
     * @param asyncResponse Suspended asynchronous response (injected).
     * @param requestId Header identifying the header.
     */
    @GET
    public void getMessage(@Suspended final AsyncResponse asyncResponse, final @HeaderParam("request-id") String requestId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // Put actual response to the queue. This response will be later taken and resumed with
                    // the message.
                    suspended.put(new AsyncResponseWrapper(asyncResponse, requestId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Handle a HTTP POST method asynchronously (suspend response in order to release the container thread instead of
     * blocking it on the blocking queue).
     *
     * @param postAsyncResponse Suspended asynchronous response (injected).
     * @param message Message to be sent.
     */
    @POST
    public void postMessage(@Suspended final AsyncResponse postAsyncResponse, final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Take one response from the queue and resume it with the message. If no message is in the queue now
                    // then this method will block the thread until the response is put into queue (by GET http method).
                    final AsyncResponseWrapper responseWrapper = suspended.take();
                    responseWrapper.getAsyncResponse().resume(Response.ok()
                            .entity(message).header("request-id", responseWrapper.getId()).build());

                    // Now resume response connected with the request invoking this post method just reply that the message
                    // was delivered.
                    postAsyncResponse.resume("Sent!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Get the string representation of internal async response queue <code>suspended<code/>.
     * <p/>
     * This resource is requested in regular intervals by clients.
     *
     * @return Plain text with list of request-id of async responses.
     */
    @GET
    @Path("queue")
    @Produces("text/html")
    public String getResponseQueue() {
        StringBuffer sb = new StringBuffer();
        boolean addSeparator = false;
        for (AsyncResponseWrapper asyncResponseWrapper : suspended) {
            if (addSeparator) {
                sb.append(", ");
            } else {
                addSeparator = true;
            }
            sb.append(asyncResponseWrapper.getId());
        }
        return sb.toString();
    }
}
