/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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
if (!JSF) {

    /**
     * The top level global namespace for JavaServer Faces Server Sent Event
     * functionality.
     * @name JSF
     * @namespace
     */
    var JSF = {};

    /**
     * The namespace for Server Sent Event functionality.
     * @name JSF.sse
     * @namespace
     * @exec
     */
    JSF.sse = function() {

        var eventSource = null;
        
        var getEventSource = function getEventSource(url) {
            url = 'http://' + document.location.host + url;
            eventSource = new EventSource(url);
        };

        return {

            /**
             * Connect to a server end point.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * JSF.sse.connect(url, {events: 'stock:stockHandler time:clockHandler'});
             * ...
             * function stockHandler(event) {
             * ...
             * }
             * </pre></code>
             *
             * @member JSF.sse
             * @param url The URL of a valid server end point that will deliver messages.
             * @param eventOptions The set of events consisting of event name:handler name pairs 
             *   associating an event name from the server, and the name of the handler that 
             *   will process the event..
             */
            connect: function connect(url, eventOptions) {
                if (url === null) {
                    throw new Error("Must specify URL");
                }
                getEventSource(url);
                if (eventSource !== null) {
                    if (eventOptions !== null) {
                        for (var i in eventOptions) {
                           JSF.sse.addOnEvent(i, eventOptions[i]);
                        }
                    }
                }

            },

            /**
             * Register a callback for error handling.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * JSF.sse.addOnError(handleError);
             * ...
             * var handleError = function handleError(data) {
             * ...
             * }
             * </pre></code>
             *
             * @member JSF.sse
             * @param callback a reference to a function to call on an error
             */
            addOnError: function addOnError(callback) {
                if (eventSource !== null) {
                    if (typeof callback === 'function') {
                        eventSource.addEventListener('error', callback, false);
                    }
                }

            },

                        /**
             * Register a callback for event handling.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * JSF.sse.addOnEvent('timeEvent', handleEvent);
             * ...
             * var handleEvent = function handleEvent(data) {
             * ...
             * }
             * </pre></code>
             *
             * @member JSF.sse
             * @param eventName the event name associated with the message.
             * @param callback a reference to a function to call for processing messages with a specific event name.
             */
            addOnEvent: function addOnEvent(eventName, callback) {
                if (eventSource !== null) {
                    if (typeof callback === 'function' && eventName !== null
                            && typeof eventName !== 'undefined') {
                        eventSource.addEventListener(eventName, callback, false);
                    }
                }

            }
        };

    }();
}

