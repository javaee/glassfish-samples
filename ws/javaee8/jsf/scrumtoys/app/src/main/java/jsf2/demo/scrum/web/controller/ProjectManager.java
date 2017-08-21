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

import jsf2.demo.scrum.model.entities.Project;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import javax.faces.context.ExternalContext;

import javax.transaction.Transactional;

import javax.persistence.PersistenceContext;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("projectManager")
@SessionScoped
public class ProjectManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Project currentProject;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void construct() {
        Project project = new Project();
        setCurrentProject(project);
    }

    @PreDestroy
    public void destroy() {
        currentProject = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("projectManager");
                }
            }
        }
    }

    public String create() {
        Project project = new Project();
        setCurrentProject(project);
        return "create";
    }

    @Transactional
    public String save() {
        Project cur = getCurrentProject();
        if (cur != null) {
            Project merged = em.merge(cur);
            if (!currentProject.equals(merged)) {
                setCurrentProject(merged);
            }
        }
        return "show";
    }

    public String edit(Project project) {
        setCurrentProject(project);
        // Using implicity navigation, this request come from /projects/show.xhtml and directs to /project/edit.xhtml
        return "edit";
    }

    @Transactional
    public String remove(final Project project) {
        if (project != null) {
            Object toRemove = em.find(Project.class, project.getId());
            assert(null != toRemove);
            
            em.remove(toRemove);
        }
        // Using implicity navigation, this request come from /projects/show.xhtml and directs to /project/show.xhtml
        // could be null instead
        return "show";
    }

    @Transactional(dontRollbackOn=ValidatorException.class)
    public void checkUniqueProjectName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        Long count;
        Query query = em.createNamedQuery((getCurrentProject().isNew()) ? "project.new.countByName" : "project.countByName");
        query.setParameter("name", newName);
        if (!currentProject.isNew()) {
            query.setParameter("currentProject", getCurrentProject());
        }
        count = (Long) query.getSingleResult();
        if (count != null && count > 0) {
            throw new ValidatorException(getFacesMessageForKey("project.form.label.name.unique"));
        }
    }

    public String cancelEdit() {
        // Implicity navigation, this request come from /projects/edit.xhtml and directs to /project/show.xhtml
        return "show";
    }

    public String showSprints(Project project) {
        setCurrentProject(project);
        // Implicity navigation, this request come from /projects/show.xhtml and directs to /project/showSprints.xhtml
        return "showSprints";
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String viewSprints() {
        return "/sprint/show";
    }


}
