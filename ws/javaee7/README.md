## Java EE 7 Sample Applications README

This README file contains step-by-step instructions which can be used to migrate existing Java EE 6 sample applications to Java EE 7 sample application workspace. Please refer to this IssueTracker comment to determine which Java EE 6 samples should be ported and their priority. Comment also contains some feedback on directory structure reorganization and renaming, so please take that into account.

You can check out combined Java EE samples workspace using this URL:

~~https://svn.java.net/svn/glassfish-samples~svn/trunk/ws~~

Java EE 6 sample applications are located in `javaee6` subdirectory and Java EE 7 samples workspace is in `javaee7` subdirectory.

To port an existing Java EE 6 sample application:

* Locate the application in Java EE 6 sample workspace and create corresponding directory structure in Java EE 7 workspace, if not already in place. While doing this, please take into account directory structure changes suggested in the IssueTracker enhancement request. For instance, to migrate `javaee6/weld/weld-guess` sample, you would create `javaee7/cdi/cdi-guess` directory and place Maven module for the sample application there.

* Create the `pom.xml` file for sample group, if not already in place. For instance, `javaee7/cdi/pom.xml`. Use one of existing sample group poms for reference, for instance `javaee7/jsf/pom.xml`. Adjust artifact name to match the technology name.

* Create the `pom.xml` file for the individual module using one of existing module poms as reference. Most of the sample apps should create Maven `war` module. It should be sufficient to use `javaee-api` as the only compile time dependency, with "provided" scope. At the end of this document you will find several links to Maven plugin and pom documentation which may be useful for this step.

* Under the application module directory create subdirectories for sample documents and code. Most samples will need these subdirectories:
 	+ `src/docs`
 	+ `src/main/java`
 	+ `src/main/resources`
 	+ `src/main/webapp`

* Copy sample documentation and code from Java EE 6 sample module to Java EE 7 sample module. In general, these are the mappings between old and new directories:
	+ `<Java EE 6 Sample dir>/docs` -> `<Java EE 7 Sample dir>/src/docs`
	+ `<Java EE 6 Sample dir>/src/java` -> `<Java EE 7 Sample dir>/src/main/java`
	+ `<Java EE 6 Sample dir>/src/resources` -> `<Java EE 7 Sample dir>/src/main/resources`
	+ `<Java EE 6 Sample dir>/web` -> `<Java EE 7 Sample dir>/src/main/webapp`

	For more guidance on recommended Maven module directory structure see links at the end of the document.

	At this time you may not be able to move `<Java EE 6 Sample dir>/test` content to the new module location since this is dependent on the implementation of new Maven based test and deployment framework. More details on this will be forecoming.

* At this point, you should be able to run `mvn install` from the module directory and build the application. Resulting war file will be present in the `target` subdirectory.

	To run your application, you can deploy it manually to a GlassFish instance using asadmin. The recommended method of telling users of the sample how to deploy it is to use Cargo. The top-level pom.xml is configured with the [Cargo](http://cargo.codehaus.org/) maven plugin. There are several ways to use it:

	+ Run it using the `cargo:run` goal - this downloads a GlassFish build from the public web site, installs it under the target directory, creates a domain, starts the server, and deploys the application.
	+ To use a local glassfish.zip file, provide a value for the cargo.maven.containerUrl property. For example: `-Dcargo.maven.containerUrl=file:$HOME/test/glassfish/glassfish.zip`
	+ To run it using an already installed GlassFish, provide a value for the glassfish.home property. For example: `-Dglassfish.home=$HOME/test/glassfish/glassfish3`

	Besides the `cargo:run` goal, which runs the application in the foreground, you can also use the `cargo:start` goal which will create a domain, start the server in the background and deploy the application. The `cargo:stop` goal can be used to stop the domain.

	If your sample requires additional configuration, such as a database, security realms, etc., make sure to configure those requirements properly. 

	If the client of your sample is a standalone Java application, you can use `org.codehaus.mojo:exec-maven-plugin:exec` to run the Java client. Take `ejb/automatic-timer/automatic-timer-client/pom.xml` for example, this sample uses exec-maven-plugin plugin to run a remote EJB client. And the client depends on the local gf-client.jar. This jar and the directory of `target/classes` will be placed on classpath when run the client.

* If things work (or at least build), add your content to the repository and commit it. If you are unable to commit changes, you may have insufficient project privileges and in that case please contact one of project owners.

## Some useful links

* [Maven standard directory layout](http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)

* [Maven dependency mechanism](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

* [Maven war plugin](http://maven.apache.org/plugins/maven-war-plugin/)
