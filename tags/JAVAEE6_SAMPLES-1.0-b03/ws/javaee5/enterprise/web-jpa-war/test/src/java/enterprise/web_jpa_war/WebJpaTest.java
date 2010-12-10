/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package enterprise.web_jpa_war;

import com.gargoylesoftware.htmlunit.html.HtmlTable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * This test class exercises the JSF/JPA demo
 * application
 */
public class WebJpaTest {

    // System property values used to configure the HTTP connection
    private String contextPath = "/web-jpa-war";
    private String host = null;
    private int port = 0;

    // The current session identifier
    private String sessionId = null;


    // The WebClient instance for this test case
    private WebClient client = null;

    // ------------------------------------------------------------ Init Methods


    /** Set up instance variables required by this test case. */
    @Before
    public void setUp() throws Exception {

        host = System.getProperty("javaee.server.name");
        port = Integer.parseInt(System.getProperty("javaee.server.port"));

        client = new WebClient();
        client.setRedirectEnabled(true);

    }

    // ------------------------------------------------------------ Test Methods


    /**
     * This test asserts following:
     * 1. The create person form creates a person record in database created person 
     * 2. The list person page shows the newly created user
     *
     * The test using following approach: 
     * Step 1 : The list person page is called and all current Ids on the page are scanned
     * Step 2 : A random person id is created that is not in the list
     * Step 3 : The Create Person link on the page is clicked to get a form to create person
     * Step 4 : The form is submitted with generated random id above
     * Step 5 : The list person page is scanned to see that the recod gets created.
     */
    @Test
    public void webJpaTest() throws Exception {

        //Get the index page which redirects to ListPerson servlet.
        // Step 1 : The list person page is called and all current Ids on the page are scanned
        HtmlPage listPersonPage = testInitialListPerson();
        
        // Step 2 : The list person page is called and all current Ids on the page are scanned
        String newPersonId = generateRandomID(listPersonPage);
        String firstName   =  newPersonId + "first";
        String lastName    =  newPersonId + "last";
        
        // Step 3 : The Create Person link on the page is clicked to get a form to create person
        // Step 4 : The form is submitted with generated random id above
        // Get the resultant listPersonPage
        listPersonPage = testCreatePerson(listPersonPage, newPersonId, firstName, lastName);
        
        // Step 5 : The list person page is scanned to see that the recod gets created.
        testUserListed(listPersonPage, newPersonId, firstName, lastName);
    }
    
    private HtmlPage testInitialListPerson() throws Exception {
        HtmlPage listPersonPage = getPage("/index.jsp");

        // there is only one link on this page
        HtmlAnchor firstLink = (HtmlAnchor) listPersonPage.getAnchors().get(0);
        Assert.assertNotNull("Unable to find link on index page", firstLink);
        Assert.assertTrue("Link text isn't correct",
                          "Create a Person Record".equals(firstLink.asText()));
        
        return listPersonPage;
    }
    
    private String generateRandomID(HtmlPage listPersonPage) {
        HtmlTable personListTable = getPersonListTable(listPersonPage);
        List<HtmlTableRow> tableRows = (List<HtmlTableRow>) personListTable.getRows();
        List<String> personIds = new ArrayList<String>();
        for(HtmlTableRow tableRow : tableRows) {
            //The person Id is contained in first column
            personIds.add(tableRow.getCell(0).asText());
        }
        
        Random randomGenerator = new Random();
        final int maxTries = 1000;
        String generatedPersonId = null;
        for (int i = 0 ; i < maxTries && generatedPersonId == null ; i++) {
            int id = randomGenerator.nextInt();
            String idString = String.valueOf(id);
            if (!personIds.contains(idString) ) {
                generatedPersonId = idString;
            }
        }
        Assert.assertNotNull("Not able to generated a unique id for test. Please clean the database and rerun the test", generatedPersonId);
        
        return generatedPersonId;
    }
    
    private HtmlPage testCreatePerson(HtmlPage listPersonPage, 
            String newPersonId, String firstName, String lastName) throws Exception {
        System.out.println("Testing Create Person with personId: " + newPersonId + 
                " firstName: " + firstName + " lastName: " + lastName);
        HtmlAnchor createPersonAnchor = (HtmlAnchor) listPersonPage.getAnchors().get(0);
        HtmlPage createPersonPage = (HtmlPage) createPersonAnchor.click();
        String expectedTitle = "Create a Person Record";
        Assert.assertTrue("Title is not '" + expectedTitle + "'",
                          expectedTitle.equals(createPersonPage.getTitleText()));
        
        //Populate the form on the page
        HtmlTextInput id =
              (HtmlTextInput) getInputContainingGivenId(createPersonPage, "id");
        Assert.assertNotNull("Unable to find id input field", id);
        id.setValueAttribute(newPersonId);

        HtmlInput firstNameInput =
              (HtmlInput) getInputContainingGivenId(createPersonPage, "firstName");
        Assert.assertNotNull("Unable to find firstName input field", firstNameInput);
        firstNameInput.setValueAttribute(firstName);
        
        HtmlInput lastNameInput =
              (HtmlInput) getInputContainingGivenId(createPersonPage, "lastName");
        Assert.assertNotNull("Unable to find lastName input field", lastNameInput);
        lastNameInput.setValueAttribute(lastName);

        HtmlSubmitInput submit =
              (HtmlSubmitInput) getInputContainingGivenId(createPersonPage, "CreateRecord");
        Assert.assertNotNull("Unable to find submit button", submit);
        HtmlPage resultListPersonPage =  (HtmlPage) submit.click();

        System.out.println("Done");
        return resultListPersonPage;
    }

    private void testUserListed(HtmlPage listPersonPage, String expectedPersonId, 
            String expectedFirstName, String expectedLastName) {
        
        System.out.println("Testing List Person with personId: " + expectedPersonId + 
            " firstName: " + expectedFirstName + " lastName: " + expectedLastName);
        HtmlTable personListTable = getPersonListTable(listPersonPage);
        List<HtmlTableRow> tableRows = (List<HtmlTableRow>) personListTable.getRows();
        boolean personListed = false;
        for(HtmlTableRow tableRow : tableRows) {
            //The person Id is contained in first column
            String personId = tableRow.getCell(0).asText();
            if(expectedPersonId.equals(personId)) {
                String firstName = tableRow.getCell(1).asText();
                String lastName  = tableRow.getCell(2).asText();
                
                if (expectedFirstName.equals(firstName) && expectedLastName.equals(lastName)) {
                    personListed = true;
                    break;
                }
            }
        }
        
        Assert.assertTrue("Could not find expected person record in the list ", personListed);
        System.out.println("Done");
    }
    
    private HtmlTable getPersonListTable(HtmlPage listPersonPage) {
        final String personListTableName = "personListTable";
        final HtmlTable personListTable = (HtmlTable)listPersonPage.getHtmlElementById(personListTableName);
        Assert.assertNotNull("Unable to find " + personListTableName + " on list person page", personListTable);
        return personListTable;
    }

    
  // -------------------------------------  Private helper methods around HtmlUnit

    private HtmlPage getPage(String path) throws Exception {

        Object obj = client.getPage(getURL(path));
        HtmlPage page = (HtmlPage) obj;
        if (sessionId == null) {
            parseSession(page);
        }
        return (page);

    }


    private void parseSession(HtmlPage page) {

        String value =
              page.getWebResponse().getResponseHeaderValue("Set-Cookie");
        if (value == null) {
            return;
        }
        int equals = value.indexOf("JSESSIONID=");
        if (equals < 0) {
            return;
        }
        value = value.substring(equals + "JSESSIONID=".length());
        int semi = value.indexOf(";");
        if (semi >= 0) {
            value = value.substring(0, semi);
        }
        sessionId = value;
        //        System.err.println("Beginning session " + sessionId);

    }


    private URL getURL(String path) throws Exception {

        StringBuffer sb = new StringBuffer("http://");
        sb.append(host);
        if (port != 80) {
            sb.append(":");
            sb.append("").append(port);
        }
        sb.append(contextPath);
        sb.append(path);
        return (new URL(sb.toString()));

    }


    private List<HtmlElement> getAllElementsOfGivenClass(HtmlPage root,
                                                         List<HtmlElement> list,
                                                         Class matchClass) {

        return getAllElementsOfGivenClass(root.getDocumentElement(),
                                          list,
                                          matchClass);

    }


    private List<HtmlElement> getAllElementsOfGivenClass(HtmlElement root,
                                                         List<HtmlElement> list,
                                                         Class matchClass) {
        if (null == root) {
            return list;
        }
        if (null == list) {
            list = new ArrayList<HtmlElement>();
        }
        Iterator iter = root.getAllHtmlChildElements();
        while (iter.hasNext()) {
            getAllElementsOfGivenClass((HtmlElement) iter.next(), list,
                                       matchClass);
        }
        if (matchClass.isInstance(root)) {
            if (!list.contains(root)) {
                list.add(root);
            }
        }
        return list;
    }


    private HtmlInput getInputContainingGivenId(HtmlPage root,
                                                String id) {

        HtmlInput result = null;

        List<HtmlElement> list =
              getAllElementsOfGivenClass(root, null, HtmlInput.class);
        for (HtmlElement aList : list) {
            result = (HtmlInput) aList;
            if (-1 != result.getIdAttribute().indexOf(id)) {
                break;
            }
            result = null;
        }
        return result;

    }
}
