/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package samples.connectors.mailconnector.ra.inbound;

import javax.resource.*;
import javax.resource.spi.*;

/**
 * This class implements the Activation Spec class
 * of the Sample mailconnector connector.
 *
 * @author Alejandro Murillo
 *
 */

@Activation(
        messageListeners = {samples.connectors.mailconnector.api.JavaMailMessageListener.class}
        
)
public class ActivationSpecImpl implements javax.resource.spi.ActivationSpec, 
                                           java.io.Serializable
{
    private ResourceAdapter resourceAdapter = null;
    /**
     * TODO : Use bean validation mechanism to specify "required-config-property" equivalent of
     * deployment descriptor (ra.xml) in the bean via @NotNull
     */
    @ConfigProperty()
    // serverName property value
    private String serverName = new String("");

    @ConfigProperty()
    // userName property value
    private String userName = new String("");

    @ConfigProperty()
    // password property value
    private String password = new String("");

    @ConfigProperty()
    // folderName property value
    private String folderName = new String("Inbox");
    
    // protocol property value
    // Normally imap or pop3
    @ConfigProperty(
            description = "Normally imap or pop3"
    )
    private String protocol = new String("imap");
    
    // Polling interval (seconds)
    private String interval = new String("30");
    
    /**
     * Constructor. Creates a new instance of the base activation spec.
     */

    public ActivationSpecImpl() { }

    /**
     * Returns the value of the serverName property.
     *
     * @return    String containing the value of the serverName 
     *            property
     */

    public String getServerName() 
    {
        return this.serverName;
    }

    /**
     * Sets the value of the serverName property.
     *
     * @param serverName  String containing the value to be assigned 
     *                    to serverName
     */

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    /**
     * Returns the value of the userName property.
     *
     * @return   String containing the value of the userName property
     */

    public String getUserName() 
    {
        return this.userName;
    }

    /**
     * Sets the value of the userName property.
     *
     * @param userName    String containing the value to be assigned 
     *                    to userName
     */

    public void setUserName(String userName)
    {
            this.userName = userName;
    }

    /**
     * Returns the value of the password property.
     *
     * @return    String containing the value of the password property
     */

    public String getPassword() 
    {
        return this.password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param password    String containing the value to be 
     *                    assigned to password
     */

    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Returns the value of the folderName property.
     *
     * @return  String containing the value of the folderName property
     */

    public String getFolderName() 
    {
        return this.folderName;
    }

    /**
     * Sets the value of the folderName property.
     *
     * @param folderName    String containing the value to be assigned
     *                      to folderName
     */

    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    /**
     * Returns the value of the protocol property.
     *
     * @return    String containing the value of the protocol property
     */

    public String getProtocol() 
    {
        return this.protocol;
    }

    /**
     * Sets the value of the protocol property.
     *
     * @param protocol    String containing the value to be assigned 
     *                    to protocol
     */

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    /**
     * Returns the value of the interval property.
     *
     * @return    String containing the value of the interval property
     */

    public String getInterval() 
    {
        return this.interval;
    }

    /**
     * Sets the value of the interval property.
     *
     * @param interval    String containing the value to be assigned 
     *                    to interval
     */

    public void setInterval(String interval) 
    {
        this.interval = interval;
    }

    /**
     * Validates the configuration properties.
     * TBD: verify that a connection to the mail server can be done
     *
     * @exception    InvalidPropertyException
     */

    public void validate() 
	throws InvalidPropertyException 
    { }

    /**
     * Sets the resource adapter.
     *
     * @param ra  the resource adapter
     */

    public void setResourceAdapter(ResourceAdapter ra)
        throws ResourceException
    {
        this.resourceAdapter = ra;
    }

    /**
     * Gets the resource adapter.
     *
     * @return   the resource adapter
     */
    public ResourceAdapter getResourceAdapter()
    {
        return resourceAdapter;
    }

}
