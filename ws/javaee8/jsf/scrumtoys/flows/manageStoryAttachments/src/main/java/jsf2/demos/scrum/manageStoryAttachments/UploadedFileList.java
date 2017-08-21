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

package jsf2.demos.scrum.manageStoryAttachments;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.UploadedFile;
import jsf2.demo.scrum.web.controller.StoryManager;

/**
 * @author Eder Magalhaes
 */
@Named("uploadedFileList")
@ViewScoped
public class UploadedFileList implements Serializable {
    
    @Inject StoryManager storyManager;
    
    private DataModel<UploadedFile> uploadedFiles;
    
    private List<UploadedFile> uploadedFileList;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    @Transactional
    public void init() {
        List<UploadedFile> result = storyManager.getCurrentStory().getUploadedFiles();
        setUploadedFileList(result);
    }
    
    public DataModel<UploadedFile> getUploadedFiles() {
        this.uploadedFiles = new ListDataModel<UploadedFile>(uploadedFileList);
        return this.uploadedFiles;
    }

    public List<UploadedFile> getUploadedFileList() {
        return uploadedFileList;
    }
    
    public void setUploadedFiles(DataModel<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public void setUploadedFileList(List<UploadedFile> uploadedFileList) {
        this.uploadedFileList = uploadedFileList;
    }
    
    @Transactional
    public String remove() {
        Story managedStory = em.find(Story.class, storyManager.getCurrentStory().getId());
        UploadedFile managedFile = em.find(UploadedFile.class, uploadedFiles.getRowData().getId());
        
        managedStory.removeUploadedFile(managedFile);
        managedFile.setStory(null);
        storyManager.setCurrentStory(managedStory);
        
        em.remove(managedFile);
        
        return "show";
        
    }

}
