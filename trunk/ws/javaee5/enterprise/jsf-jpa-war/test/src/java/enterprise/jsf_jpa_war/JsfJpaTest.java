/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package enterprise.jsf_jpa_war;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * This test class exercises the JSF/JPA demo
 * application
 */
public class JsfJpaTest {

    // System property values used to configure the HTTP connection
    private String contextPath = "/jsf-jpa-war";
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


    @Test
    public void jsfJpaTest() throws Exception {

        // Step 1: Get the index page and 'click' the link
        HtmlPage page = getPage("/index.jsp");

        // there is only one link on this page
        HtmlAnchor firstLink = (HtmlAnchor) page.getAnchors().get(0);
        Assert.assertNotNull("Unable to find link on index page", firstLink);
        Assert.assertTrue("Link text isn't correct",
                          "Java Server Faces Welcome Page".equals(firstLink.asText()));
        page = (HtmlPage) firstLink.click();

        // Step 2: We're at the login page
        //         Verify an error message is returned if we login
        //         with an unknown name
        Assert.assertTrue("Title is not 'Login'",
                          "Login".equals(page.getTitleText()));
        LoginForm form = getLoginForm(page);
        form.setUserName("nosuchuser");
        form.setPassowrd("nosuchuser");
        page = form.submit();

        Assert.assertTrue(
              "Unable to find appropriate error message for invalid user",
              page.asText().contains(
                    "Login Failed! Username 'nosuchuser' does not exist."));

        // Step 3: Create a new user
        HtmlAnchor secondLink = (HtmlAnchor) page.getAnchors().get(0);
        page = (HtmlPage) secondLink.click();
        Assert.assertTrue("Title is not 'Create Account'",
                          "Create Account".equals(page.getTitleText()));
        NewUserForm newUser = getNewUserForm(page);
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setUserName("username");
        newUser.setPassword("password");
        newUser.setPassword2("password");
        page = newUser.submit();

        // Step 4: Login and validate we end up on the welcome page         
        Assert.assertTrue("Title is not 'Login'",
                          "Login".equals(page.getTitleText()));
        LoginForm form2 = getLoginForm(page);
        form2.setUserName("username");
        form2.setPassowrd("password");
        page = form2.submit();
        Assert.assertTrue("Unable to find expected welcome text after logging in",
                          page.asText().contains("Welcome First Last!"));
    }

    // --------------------------------------------------------- Private Methods

    private NewUserForm getNewUserForm(HtmlPage page) {

        HtmlTextInput fname =
              (HtmlTextInput) getInputContainingGivenId(page, "fname");
        Assert.assertNotNull("Unable to find first name input field", fname);

        HtmlTextInput lname =
              (HtmlTextInput) getInputContainingGivenId(page, "lname");
        Assert.assertNotNull("Unable to find last name input field", lname);

        HtmlTextInput username =
              (HtmlTextInput) getInputContainingGivenId(page, "username");
        Assert.assertNotNull("Unable to find username input field", username);

        HtmlPasswordInput password =
              (HtmlPasswordInput) getInputContainingGivenId(page, "password");
        Assert.assertNotNull("Unable to find password input field", password);

        HtmlPasswordInput password2 =
              (HtmlPasswordInput) getInputContainingGivenId(page, "passwordv");
        Assert.assertNotNull("Unable to find password (verify) input field",
                             password2);

        HtmlSubmitInput submit =
              (HtmlSubmitInput) getInputContainingGivenId(page, "submit");
        Assert.assertNotNull("Unable to find submit button", submit);

        return new NewUserForm(fname,
                               lname,
                               username,
                               password,
                               password2,
                               submit);
    }


    private LoginForm getLoginForm(HtmlPage page) {

        HtmlTextInput username =
              (HtmlTextInput) getInputContainingGivenId(page, "username");
        Assert.assertNotNull("Unable to find username input field", username);

        HtmlPasswordInput password =
              (HtmlPasswordInput) getInputContainingGivenId(page, "password");
        Assert.assertNotNull("Unable to find password input field", password);

        HtmlSubmitInput submit =
              (HtmlSubmitInput) getInputContainingGivenId(page, "submit");
        Assert.assertNotNull("Unable to find submit button", submit);

        return new LoginForm(username, password, submit);
    }

   
    private HtmlPage getPage(String path) throws Exception {

        /* Cookies seem to be maintained automatically now
        if (sessionId != null) {
            //            System.err.println("Joining   session " + sessionId);
            client.addRequestHeader("Cookie", "JSESSIONID=" + sessionId);
        }
        */
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

    
    // ---------------------------------------------------------- PrivateClasses


    private static class LoginForm {

        private HtmlTextInput username;
        private HtmlPasswordInput password;
        private HtmlSubmitInput submit;

        public LoginForm(HtmlTextInput username,
                         HtmlPasswordInput password,
                         HtmlSubmitInput submit) {

            this.username = username;
            this.password = password;
            this.submit = submit;
        }

        public void setUserName(String name) {
            username.setValueAttribute(name);
        }

        public void setPassowrd(String pass) {
            password.setValueAttribute(pass);
        }

        public HtmlPage submit() throws IOException {
            return (HtmlPage) submit.click();
        }

    }


    private static class NewUserForm {

        private HtmlTextInput firstName;
        private HtmlTextInput lastName;
        private HtmlTextInput username;
        private HtmlPasswordInput password;
        private HtmlPasswordInput password2;
        private HtmlSubmitInput submit;

        public NewUserForm(HtmlTextInput firstName,
                           HtmlTextInput lastName,
                           HtmlTextInput username,
                           HtmlPasswordInput password,
                           HtmlPasswordInput password2,
                           HtmlSubmitInput submit) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.password2 = password2;
            this.submit = submit;

        }

        public void setFirstName(String fname) {
            firstName.setValueAttribute(fname);
        }

        public void setLastName(String lname) {
            lastName.setValueAttribute(lname);
        }

        public void setUserName(String name) {
            username.setValueAttribute(name);
        }

        public void setPassword(String pass) {
            password.setValueAttribute(pass);
        }

        public void setPassword2(String pass) {
            password2.setValueAttribute(pass);
        }

        public HtmlPage submit() throws IOException {
            return (HtmlPage) submit.click();
        }
    }


} // END JsfJpaTest
