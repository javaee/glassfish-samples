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

package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PreDestroy;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;


/**
 * @author edermag
 */
@Named("skinValuesManager")
@ApplicationScoped
public class SkinValuesManager implements Serializable {

    private Map<String, String> values;
    private List<ColorTuple> displayNames;

    private final String defaultSkin = "blue";
    private static final long serialVersionUID = 2238251086172648511L;

    @PostConstruct
    public void construct() {
        values = new LinkedHashMap<String, String>();
        displayNames = new ArrayList<ColorTuple>();
        
        ViewDeclarationLanguageFactory vdlFactory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);
        ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage("home.xhtml");
        FacesContext context = FacesContext.getCurrentInstance();
        List<String> availableContracts = vdl.calculateResourceLibraryContracts(context, "home.xhtml");
        
        
        String key;
        String displayName;
        int len;
        for (String cur : availableContracts) {
            len = cur.length();
            displayName = cur.substring(3, len - 4);
            key = displayName.toLowerCase();
            values.put(key, cur + ".css");
            displayNames.add(new ColorTuple(displayName, key));
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (null != values) {
            values.clear();
            values = null;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("skinValuesManager");
                }
            }
        }
    }
    
    protected String getSkinCss(String skin) {
        if (!values.containsKey(skin))
            return getDefaultSkinCss();
        return values.get(skin);
    }

    protected String getDefaultSkinCss() {
        return values.get(defaultSkin);
    }

    public List<ColorTuple> getColorTuples() {
        return displayNames;
    }
    
    public static class ColorTuple {
        private String displayName;
        private String value;

        public ColorTuple(String displayName, String value) {
            this.displayName = displayName;
            this.value = value;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getValue() {
            return value;
        }
        
        
    }
    

    public int getSize() {
        return values.keySet().size();
    }

}
