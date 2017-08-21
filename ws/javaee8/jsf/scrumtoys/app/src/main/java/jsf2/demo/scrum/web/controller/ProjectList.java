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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Project;

/**
 * @author Eder Magalhaes
 */
@Named("projectList")
@ViewScoped
public class ProjectList extends AbstractManager implements Serializable{

    @Inject
    private ProjectManager projectManager;

    private DataModel<Project> projects;
    private List<SelectItem> projectItems;
    private List<Project> projectList;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        Query query = em.createNamedQuery("project.getAll");
        setProjectList((List<Project>) query.getResultList());
        projectItems = new LinkedList<SelectItem>();
        projectItems.add(new SelectItem(new Project(), "-- Select one project --"));
        if (getProjectList() != null) {
            projects = new ListDataModel<Project>(getProjectList());
            for (Project p : getProjectList()) {
                projectItems.add(new SelectItem(p, p.getName()));
            }
        }
    }
    
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public DataModel<Project> getProjects() {
        return projects;
    }

    public void setProjects(DataModel<Project> projects) {
        this.projects = projects;
    }

    public List<SelectItem> getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(List<SelectItem> projectItems) {
        this.projectItems = projectItems;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public String edit() {
        return projectManager.edit(projects.getRowData());
    }

    public String remove() {
        String result = projectManager.remove(projects.getRowData());
        init();
        return result;
    }

    public String showSprints() {
        return projectManager.showSprints(projects.getRowData());
    }
}
