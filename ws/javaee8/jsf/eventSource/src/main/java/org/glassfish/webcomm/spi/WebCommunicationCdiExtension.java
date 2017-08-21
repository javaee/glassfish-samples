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

package org.glassfish.webcomm.spi;

import org.glassfish.webcomm.annotation.WebHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;
import org.glassfish.webcomm.annotation.WebMessageProcessor;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebSocketHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebCommunicationExtension class
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Jitendra Kotamraju
 */
public class WebCommunicationCdiExtension implements Extension {

    private WebCommunicationProvider provider;

    private final Map<String, WebCommunicationContext> contexts
        = new ConcurrentHashMap<String, WebCommunicationContext>();

    public WebCommunicationCdiExtension() {
        ServiceLoader<WebCommunicationProvider> wcp =
                ServiceLoader.load(WebCommunicationProvider.class);
        Iterator<WebCommunicationProvider> it = wcp.iterator();
        assert it.hasNext();
        provider = it.next();
    }

    public WebCommunicationProvider getProvider() {
        return provider;
    }

    public Map<String, WebCommunicationContext> getContexts() {
        return contexts;
    }

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
        provider.setBeanManager(bm);
        bbd.addQualifier(WebHandlerContext.class);
    }

    static class WebHandlerContextAnnotationLiteral extends AnnotationLiteral<WebHandlerContext> implements WebHandlerContext {
        private final String path;

        WebHandlerContextAnnotationLiteral(String path) {
            this.path = path;
        }

        @Override
        public String value() {
            return path;
        }
    }

    static class WebCommunicationContextBean implements Bean<WebCommunicationContext> {

        private final String path;
        private final WebCommunicationContext instance;

        WebCommunicationContextBean(String path, WebCommunicationContext instance) {
            this.path = path;
            this.instance = instance;
        }

        @Override
        public Set<Type> getTypes() {
            Set<Type> types = new HashSet<Type>();
            types.add(WebCommunicationContext.class);
            return types;
        }

        @Override
        public Set<Annotation> getQualifiers() {
            Set<Annotation> qualifiers = new HashSet<Annotation>();
            qualifiers.add(new WebHandlerContextAnnotationLiteral(path));
            return qualifiers;
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return ApplicationScoped.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        @Override
        public Class<?> getBeanClass() {
            return WebCommunicationContext.class;
        }

        @Override
        public boolean isAlternative() {
            return false;
        }

        @Override
        public boolean isNullable() {
            return false;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        @Override
        public WebCommunicationContext create(CreationalContext<WebCommunicationContext> context) {
            return instance;
        }

        @Override
        public void destroy(WebCommunicationContext instance, CreationalContext<WebCommunicationContext> context) {
        }
    }

    // For each WebHandler, it creates a corresponding WebCommunicationContext
    // This WebCommunicationContext can be got anywhere from BeanManager
    void afterBeanDiscovery(@Observes AfterBeanDiscovery bbd) {
        for(Map.Entry<String, WebCommunicationContext> entry : contexts.entrySet()) {
            bbd.addBean(new WebCommunicationContextBean(entry.getKey(), entry.getValue()));
        }
    }

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat,
            BeanManager beanManager) {
        System.out.println("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());

        for (Annotation an : pat.getAnnotatedType().getAnnotations()) {
            Class clazz = pat.getAnnotatedType().getJavaClass();

            if (an instanceof WebHandler) {
                WebHandler wh = (WebHandler) an;
                String path = normalizePath(wh.value());

                // Get or create communication context for this path
                WebCommunicationContext wcc = contexts.get(path);
                if (wcc == null) {
                    wcc = provider.createWebCommunicationContext(path);
                    contexts.put(path, wcc);
                }

                if (WebSocketHandler.class.isAssignableFrom(clazz)) {
                    provider.registerWSHandlerClass(clazz, contexts, path);
                } else if (ServerSentEventHandler.class.isAssignableFrom(clazz)) {
                    provider.registerSSEHandlerClass(clazz, contexts, path);
                } else {
                    throw new RuntimeException("Invalid base class '"
                            + clazz.getName() + "' for handler");
                }
            } else if (an instanceof WebMessageProcessor) {
                WebMessageProcessor wmp = (WebMessageProcessor) an;
                provider.registerMessageProcessor(wmp.value(), clazz);
            }
        }
    }

    private String normalizePath(String path) {
        path = path.trim();
        return path.startsWith("/") ? path : "/" + path;
    }

}
