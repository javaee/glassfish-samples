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
package jsf2.demos.scrum.manageStoryAttachments;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.UploadedFile;
import jsf2.demo.scrum.web.controller.StoryManager;

@Named
@FlowScoped(definingDocumentId="manageStoryAttachments", value="manageStoryAttachments")
public class UploadManager implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private StoryManager storyManager;
    
    private Part uploadedFile;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part part) {
        this.uploadedFile = part;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bits = new BufferedInputStream(part.getInputStream());
            byte [] buffer = new byte[8192];
            int num = 0;
            while (-1 != (num = bits.read(buffer))) {
                baos.write(buffer, 0, num);
            }
            
            UploadedFile entity = new UploadedFile();
            Story currentStory = storyManager.getCurrentStory();
            entity.setStory(currentStory);
            entity.setFileName(part.getName());
            entity.setBytes(baos.toByteArray());
            currentStory.getUploadedFiles().add(entity);

        } catch (Exception e) {
           Logger.getLogger (UploadManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException ex) {
                    Logger.getLogger (UploadManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        

    }
    
    
    
    
}
