/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package jsf2.demo.scrum.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewDeclarationLanguageWrapper;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

public class SkinManagerVDLFactory extends ViewDeclarationLanguageFactory {
    
    private ViewDeclarationLanguageFactory wrapped;
    @Inject
    private SkinManager skinManager;

    public SkinManagerVDLFactory(ViewDeclarationLanguageFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ViewDeclarationLanguageFactory getWrapped() {
        return this.wrapped;
    }
    
    public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
        return new VDL(wrapped.getViewDeclarationLanguage(viewId));
    }

    private class VDL extends ViewDeclarationLanguageWrapper {

        private ViewDeclarationLanguage wrapped;

        private VDL(ViewDeclarationLanguage wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ViewDeclarationLanguage getWrapped() {
            return wrapped;
        }

        @Override
        public List<String> calculateResourceLibraryContracts(FacesContext context, String viewId) {
            List<String> result = getWrapped().calculateResourceLibraryContracts(context, viewId);
            String selectedSkin = SkinManagerVDLFactory.this.skinManager.getSelectedSkin();
            if (null != selectedSkin) {
                assert(selectedSkin.endsWith(".css"));
                selectedSkin = selectedSkin.substring(0, selectedSkin.length() - 4);
                for (String cur : result) {
                    if (selectedSkin.equals(cur)) {
                        result = new ArrayList<String>(1);
                        result.add(cur);
                        break;
                    }
                }
            }

            return result;
        }

    }
    
    
}
