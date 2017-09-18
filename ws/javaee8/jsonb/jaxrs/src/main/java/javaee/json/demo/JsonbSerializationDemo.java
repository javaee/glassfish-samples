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

package javaee.json.demo;

import javaee.json.demo.model.person.Person;
import javaee.json.demo.model.person.PhoneNumber;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates JSON-B serialization
 * <p>
 * 1. Using of default mapping
 * 2. Using customizations
 * 3. Using formatting
 * 4. Using JsonbProperty and JsonbNillable annotations
 *
 * @author Dmitry Kornilov
 */
@Path("/serialization")
public class JsonbSerializationDemo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String doGet() {
        List<Person> jasons = new ArrayList<>(3);

        List<PhoneNumber> jasonBournePhones = new ArrayList<>(2);
        jasonBournePhones.add(new PhoneNumber("home", "123 456 789"));
        jasonBournePhones.add(new PhoneNumber("work", "123 555 555"));

        jasons.add(new Person("Jason Bourne", "Super Agent", jasonBournePhones));

        List<PhoneNumber> jasonVoorheesPhones = new ArrayList<>(1);
        jasonVoorheesPhones.add(new PhoneNumber("home", "666 666 666"));

        jasons.add(new Person("Jason Voorhees", "Maniac Killer", jasonVoorheesPhones));
        jasons.add(new Person("Jason", "Argonauts Leader", null));

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        Jsonb jsonb = JsonbBuilder.create(config);
        return jsonb.toJson(jasons);
    }

}
