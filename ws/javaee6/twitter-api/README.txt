Maven coordinates
-----------------

<dependency>
   <groupId>org.glassfish.samples</groupId>
   <artifactId>twitter-api</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>

Additional Dependencies 
-----------------------

The implementation of this API uses Jersey OAuth Filters for authentication with Twitter. 
The following additional dependencies are required if any API that requires 
authentication is used.

<dependency>
    <groupId>com.sun.jersey.contribs.jersey-oauth</groupId>
    <artifactId>oauth-client</artifactId>
    <version>1.11</version>
</dependency>
<dependency>
    <groupId>com.sun.jersey.contribs.jersey-oauth</groupId>
    <artifactId>oauth-signature</artifactId>
    <version>1.11</version>
</dependency> 


How to get started ?
--------------------

@Inject Twitter twitter;

in a Java EE managed component (such as Servlet).

A non-secure invocation of the API looks like

SearchResults result = twitter.search("glassfish", SearchResults.class);
for (SearchResultsTweet t : result.getResults()) {
 out.println(t.getText() + "<br/>");
}



How to view the REST payload ?
------------------------------
Implementation of the API uses JDK logging. The logger name is:

org.glassfish.samples.twitter.api

If the project is deployed on GlassFish 3.x, then add the following line to 
glassfish/domains/domain1/config/logging.properties

org.glassfish.samples.twitter.api.level=<LEVEL>

where <LEVEL> can be SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST

