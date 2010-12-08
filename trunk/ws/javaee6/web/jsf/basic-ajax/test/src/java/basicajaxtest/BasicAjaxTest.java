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

package basicajaxtest;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

public class BasicAjaxTest {

    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        Assert.assertEquals("JavaServer Faces 2.0 Basic Ajax Demo", page.getTitleText());
    }

    @Test
    public void checkSimpleCount() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage homepage = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        HtmlTable table = (HtmlTable) homepage.getElementById("demo-table");
        HtmlTableCell cell = table.getCellAt(1,2);
        HtmlAnchor link = (HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0);
        HtmlPage demopage = link.click();
        Assert.assertEquals("0",demopage.getHtmlElementById("out1").getTextContent());
        HtmlSubmitInput button = demopage.getHtmlElementById("button1");
        demopage = button.click();
        Assert.assertEquals("1",demopage.getHtmlElementById("out1").getTextContent());
    }

    @Test
    public void checkSecondEcho() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage homepage = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        HtmlTable table = (HtmlTable) homepage.getElementById("demo-table");
        HtmlTableCell cell = table.getCellAt(4,2);
        HtmlAnchor link =  (HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0);
        HtmlPage demopage = link.click();
        Assert.assertEquals("hello",demopage.getHtmlElementById("form1:out1").getTextContent());
        HtmlTextInput input = demopage.getHtmlElementById("form1:in1");
        input.setValueAttribute("hello test");
        HtmlButtonInput button = demopage.getHtmlElementById("form1:button1");
        demopage = button.click();
        Assert.assertEquals("hello test",demopage.getHtmlElementById("form1:out1").getTextContent());
    }
}
