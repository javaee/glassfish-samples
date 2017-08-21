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
package filesystemrlc;

import java.io.File;
import java.util.List;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

public class FilesystemResourceHandler extends ResourceHandlerWrapper {
        
    private final File resourceDirectory;
    
    private final ResourceHandler resourceHandler;

    public FilesystemResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
        this.resourceDirectory = new File("/tmp/contracts");
    }

    @Override
    public ResourceHandler getWrapped() {
        return resourceHandler;
    }

    @Override
    public Resource createResource(String resourceName) {
        return resourceHandler.createResource(resourceName);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        
        List<String> activeContracts = FacesContext.getCurrentInstance().getResourceLibraryContracts();

        if (!resourceName.startsWith("filesystemResourceHandler")) {
            if (activeContracts != null) {
                for(File file : this.resourceDirectory.listFiles()) {
                    for (String contract : activeContracts) {
                        if (contract.equals(file.getName())) {
                            if (libraryName != null) {
                                return new FilesystemResource(resourceDirectory, contract, libraryName, resourceName);
                            } else {
                                return new FilesystemResource(resourceDirectory, contract, null, resourceName);                    
                            }
                        }
                    }
                }
            }
        } else {
            resourceName = resourceName.substring(resourceName.indexOf("/") + 1);
            String contract = resourceName.substring(0, resourceName.indexOf("/"));
            resourceName = resourceName.substring(resourceName.indexOf("/") + 1);
            return new FilesystemResource(resourceDirectory, contract, null, resourceName);
        }
        
        return resourceHandler.createResource(resourceName, libraryName);
    }

    @Override
    public boolean isResourceURL(String url) {
        if (url.contains("filesystemResourceHandler")) {
            return true;
        }
        return resourceHandler.isResourceURL(url);
    }
}
