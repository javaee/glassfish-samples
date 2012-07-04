/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 *  The contents of this file are subject to the terms of either the GNU
 *  General Public License Version 2 only ("GPL") or the Common Development
 *  and Distribution License("CDDL") (collectively, the "License").  You
 *  may not use this file except in compliance with the License.  You can
 *  obtain a copy of the License at
 *  https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 *  or packager/legal/LICENSE.txt.  See the License for the specific
 *  language governing permissions and limitations under the License.
 *
 *  When distributing the software, include this License Header Notice in each
 *  file and include the License file at packager/legal/LICENSE.txt.
 *
 *  GPL Classpath Exception:
 *  Oracle designates this particular file as subject to the "Classpath"
 *  exception as provided by Oracle in the GPL Version 2 section of the License
 *  file that accompanied this code.
 *
 *  Modifications:
 *  If applicable, add the following below the License Header, with the fields
 *  enclosed by brackets [] replaced by your own identifying information:
 *   "Portions Copyright [year] [name of copyright owner]"
 *
 *  Contributor(s):
 *  If you wish your version of this file to be governed by only the CDDL or
 *  only the GPL Version 2, indicate your decision by adding "[Contributor]
 *  elects to include this software in this distribution under the [CDDL or GPL
 *  Version 2] license."  If you don't indicate a single choice of license, a
 *  recipient has the option to distribute your version of this file under
 *  either the CDDL, the GPL Version 2 or to extend the choice of license to
 *  its licensees as provided above.  However, if you add GPL Version 2 code
 *  and therefore, elected the GPL Version 2 license, then the option applies
 *  only if the new code is made subject to such option by the copyright
 *  holder.
 */
package org.glassfish.samples.twitter.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class Twitter implements Serializable {

    private static String CONSUMER_SECRET;
    private static String CONSUMER_KEY;
    private static final String CONSUMER_SECRET_PROPERTY = "consumerSecret";
    private static final String CONSUMER_KEY_PROPERTY = "consumerKey";
    private static final String BASE_URI = "https://api.twitter.com";
    private static final String OAUTH_BASE_URI = BASE_URI + "/oauth";
    private static final String API_URI = BASE_URI + "/1";
    private static final String REQUEST_TOKEN_URI = OAUTH_BASE_URI + "/request_token";
    private static final String ACCESS_TOKEN_URI = OAUTH_BASE_URI + "/access_token";
    private static final String AUTHORIZE_TOKEN_URI = OAUTH_BASE_URI + "/authorize";
    private static final String SEARCH_URI = "http://search.twitter.com/search.json?";
    
    private static String oauth_token;
    private static String oauth_token_secret;
    private static String screen_name;
    private static String host;
    private static String contextRoot;
    private static String mainDisplayPage;

    Client client;
    User user;
    
    long[] friends;
    long[] followers;
    Tweet[] homeTimeline;
    
    static final Logger logger = Logger.getLogger(Twitter.class.getPackage().getName());

    public Twitter() {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(config);
    }
    
    public Twitter(String consumerSecret, String consumerKey) {
        CONSUMER_SECRET = consumerSecret;
        CONSUMER_KEY = consumerKey;
    }
    
    public void authenticate(HttpServletRequest request, HttpServletResponse response) {
        if (mainDisplayPage != null) {
            return;
        }
        
        logger.log(Level.INFO, "Authenticating ...");
        String uri = "http://" + 
                request.getServerName() +
                ":" + 
                request.getServerPort() +
//                "/" +
                request.getContextPath() +
                "/login";
        try {
            //        WebResource r = client.resource(uri);
            logger.log(Level.INFO, "Sending redirect: {0}", uri);
                    response.sendRedirect(uri);
//                    logger.log(Level.FINE, "Redirected");
            //        logger.log(Level.FINE, "URI: " + r.getURI());
            //        logger.log(Level.FINE, "Response: " + res);
            //        logger.log(Level.FINE, "Response: " + res);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    public void setOAuthConsumer(String consumerSecret, String consumerKey) {
        CONSUMER_SECRET = consumerSecret;
        CONSUMER_KEY = consumerKey;
    }
    
    private static void readTwitterProperties(String host, 
            int port, 
            String contextPath, 
            String requestURI, 
            String queryString) {
        
        logger.log(Level.INFO, "readTwitterProperties({0}, {1}, {2}, {3}, {4})",
                new Object[] {host, port, contextPath, requestURI, queryString});
        Twitter.host = host + ":" + port;
        Twitter.contextRoot = contextPath;
        
        mainDisplayPage = getMainDisplayPage(requestURI, queryString);
                
        // Secret and Key are already set
        if (!((CONSUMER_SECRET == null || CONSUMER_SECRET.equals("")) &&
            (CONSUMER_KEY == null || CONSUMER_KEY.equals("")))) {
                    return;
        }
        
        // Secret and Key are not set, try to read from System properties
        Properties props = System.getProperties();
        if (!props.containsKey(CONSUMER_KEY_PROPERTY) || !props.containsKey(CONSUMER_SECRET_PROPERTY)) {
            logger.log(Level.WARNING, CONSUMER_KEY_PROPERTY + " or " + CONSUMER_SECRET_PROPERTY + " not defined in system properties.");
//            logger.log(Level.WARNING, "Existing system properties: {0}", props.toString());
        } else {
            logger.log(Level.INFO, CONSUMER_KEY_PROPERTY + " and " + CONSUMER_SECRET_PROPERTY + " properties found in system properties.");
            CONSUMER_SECRET = props.getProperty(CONSUMER_SECRET_PROPERTY);
            CONSUMER_KEY = props.getProperty(CONSUMER_KEY_PROPERTY);
            return;
        }
        
        
        // Secret and Key are not set yet, try to read from the local filesystem
        FileInputStream fis = null;
        String propsFileName = System.getProperty("user.home") + 
                System.getProperty("file.separator") + 
                ".tvitterclone";
        
        
        logger.log(Level.FINE, propsFileName);
        try {
            fis = new FileInputStream(propsFileName);
            props.load(fis);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "{0} not found.", propsFileName);
            return;
        } catch (IOException e) {
            // ignore
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
        
        if (!props.containsKey(CONSUMER_KEY_PROPERTY) || !props.containsKey(CONSUMER_SECRET_PROPERTY)) {
            logger.log(Level.SEVERE, CONSUMER_KEY_PROPERTY + " or " + CONSUMER_SECRET_PROPERTY + " not defined in {0}", propsFileName);
            return;
        }

        logger.log(Level.INFO, CONSUMER_KEY_PROPERTY + " and " + CONSUMER_SECRET_PROPERTY + " read from {0}", propsFileName);
        CONSUMER_SECRET = props.getProperty(CONSUMER_SECRET_PROPERTY);
        CONSUMER_KEY = props.getProperty(CONSUMER_KEY_PROPERTY);
    }
    
    private static String getMainDisplayPage(String requestURI, String queryString) {
        logger.log(Level.FINE, "getMainDisplayPage({0}, {1})", 
                new Object[]{requestURI, queryString});
        if (queryString == null) {
            return "/" + contextRoot + "/faces/index.xhtml";
        }
        
        StringTokenizer tokens = new StringTokenizer(queryString, "&");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.startsWith("display")) {
                return "/" + contextRoot + token.substring(token.indexOf("=")+1, token.length());
            }
        }
        
//        return requestURI;
        return "/" + contextRoot + "/faces/index.xhtml";
    }
    
    private static Form getRequestToken() {
        logger.log(Level.FINE, "getRequestToken()");
        // Create a Jersey client
        Client reqTokenClient = Client.create();

        // Create a resource to be used to make Twitter API calls
        WebResource resource = reqTokenClient.resource(REQUEST_TOKEN_URI);

        // Set the OAuth parameters
        OAuthParameters params = new OAuthParameters().
                consumerKey(CONSUMER_KEY).
                callback("http://" + host + contextRoot + "/OAuthCallback");
        OAuthSecrets secrets = new OAuthSecrets().
                consumerSecret(CONSUMER_SECRET);
        // Create the OAuth client filter
        OAuthClientFilter oauthFilter = new OAuthClientFilter(
                reqTokenClient.getProviders(), 
                params, 
                secrets);

        // Add the filter to the resource
        if (logger.isLoggable(Level.FINE)) {
            resource.addFilter(new LoggingFilter(logger));
        }
        resource.addFilter(oauthFilter);

        // make the request and print out the result
        return resource.post(Form.class);
    }
    
    private static Form getAccessToken(HttpSession session, String oauth_verifier) {
        logger.log(Level.FINE, "getAccessToken({0}, {1})",
                new Object[] {session, oauth_verifier});
        Client accessTokenClient = new Client();
        
        WebResource resource = accessTokenClient.resource(ACCESS_TOKEN_URI);
        
        OAuthParameters params = new OAuthParameters().
                consumerKey(CONSUMER_KEY).
                token((String) session.getAttribute("oauth_token")).
                verifier(oauth_verifier);
        
        OAuthSecrets secrets = new OAuthSecrets().
                consumerSecret(CONSUMER_SECRET).
                tokenSecret((String) session.getAttribute("oauth_token_secret"));
        
        OAuthClientFilter oauthFilter = new OAuthClientFilter(
                accessTokenClient.getProviders(), 
                params, 
                secrets);
        
        if (logger.isLoggable(Level.FINE)) {
            resource.addFilter(new LoggingFilter(logger));
        }
        resource.addFilter(oauthFilter);
        
        return resource.post(Form.class);
    }
    

    @WebServlet(urlPatterns = "/login")
    public static class OAuthLoginServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            logger.log(Level.FINE, "OAuthLoginServlet.doGet()");
            response.setContentType("text/html;charset=UTF-8");
            
            readTwitterProperties(request.getServerName(), request.getServerPort(), request.getContextPath(), request.getRequestURI(), request.getQueryString());
            java.io.PrintWriter out = response.getWriter();
            try {
                Form requestTokenResponse = getRequestToken();
                HttpSession session = request.getSession(true);
                session.setAttribute("oauth_token", requestTokenResponse.getFirst("oauth_token"));
                session.setAttribute("oauth_token_secret", requestTokenResponse.getFirst("oauth_token_secret"));

                out.println("<html>");
                out.println("<head>");
                out.println("<title>OAuth Login Servlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>OAuth Login Servlet at " + request.getContextPath() + "</h1>");
                String uri = AUTHORIZE_TOKEN_URI + 
                        "?" + 
                        OAuthParameters.TOKEN + "=" + requestTokenResponse.getFirst("oauth_token")
//                        + "&oauth_callback=" + String.format(CALLBACK_URI, getServerNamePort(request))
                        ;
//                logger.log(Level.FINE, uri);
                response.sendRedirect(uri);
                
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
    }
    
    @WebServlet(urlPatterns = "/OAuthCallback")
    public static class OAuthCallbackServlet extends HttpServlet {
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            logger.log(Level.FINE, "OAuthCallbackServlet.doGet()");
            response.setContentType("text/html;charset=UTF-8");
            java.io.PrintWriter out = response.getWriter();
            try {
                String oauth_verifier = request.getParameter("oauth_verifier");
                
                Enumeration<String> tokens = request.getParameterNames();
                while (tokens.hasMoreElements()) {
                    String elem = tokens.nextElement();
                    out.println(elem + ": " + request.getParameter(elem) + "<br>");
                }
                
                HttpSession session = request.getSession(true);
                Form accessTokenResponse = getAccessToken(session, oauth_verifier);
                oauth_token = accessTokenResponse.getFirst("oauth_token");
                oauth_token_secret = accessTokenResponse.getFirst("oauth_token_secret");
                screen_name = accessTokenResponse.getFirst("screen_name");
                
//                logger.log(Level.FINE, "oauth_token: " + oauth_token);
//                logger.log(Level.FINE, "oauth_token_secret: " + oauth_token_secret);
                out.println("<html>");
                out.println("<head>");
                out.println("<title>OAuth Callback Servlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>OAuth Callback Servlet at " + request.getContextPath() + "</h1>");
                                
                response.sendRedirect(mainDisplayPage);

                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
    }
    
    public User getUser() {
        logger.log(Level.FINE, "getUser()");
        
        if (user != null) {
            return user;
        }
        
        // Authenticated user
        WebResource webResource = client.resource(API_URI).path("/account/verify_credentials.json");

//        // Unauthenticated user
//        WebResource webResource = client
//                .resource(API_URI)
//                .path("/users/show.json")
//                .queryParam("screen_name", screen_name)
//                .queryParam("include_entities", "true");

        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        user = webResource.get(User.class);
        return user;
    }
    
    public Tweet[] getUserTimeline(String userId) {
        logger.log(Level.FINE, "getUserTimeline({0})", userId);
        
        WebResource webResource = client.resource(API_URI).path("/statuses/user_timeline.json");
        String uid = screen_name;
        
        if (userId != null && userId.equals("")) {
            uid = userId;
        }

//        String userId = screen_name;
        
        
        logger.log(Level.INFO, "Getting timeline for {0}", uid);
        webResource = webResource.queryParam("include_entities", "true")
            .queryParam("include_rts", "true")
            .queryParam("screen_name", uid)
            .queryParam("trim_user", "true")
            .queryParam("count", "10");

        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        Tweet[] res = webResource.get(Tweet[].class);
        
        return res;
    }
    
    /**
     * This method returns the first 800 or less stauses from the user home 
     * timeline. <code>home/timeline</code> cannot return more than 800 statuses.
     * 
     * @return 
     */
    public Tweet[] getHomeTimeline() {
        logger.log(Level.FINE, "getHomeTimeline()");
        logger.log(Level.INFO, "Getting timeline for {0}", screen_name);

        long tweetCount = user.getStatuses_count();
        int maxTweets = tweetCount > 800 ? 800: (int)tweetCount;
        
        int totalLoops = maxTweets/200 + ((maxTweets % 200 == 0) ? 0 : 1);

        List<Tweet> list = new ArrayList<Tweet>();
        for (int i=0; i < totalLoops; i++) {
            WebResource webResource = client.resource(API_URI).path("/statuses/home_timeline.json");
            logger.log(Level.INFO, "Getting {0} tweets", (200*(i+1)));

            webResource = webResource.queryParam("include_entities", "true")
                .queryParam("include_rts", "true")
                .queryParam("screen_name", screen_name)
                .queryParam("count", "200");

            webResource.addFilter(getOAuthFilter());
            if (logger.isLoggable(Level.FINE)) {
                webResource.addFilter(new LoggingFilter(logger));
            }
            
            list.addAll(Arrays.asList(webResource.get(Tweet[].class)));
        }
        return list.toArray(new Tweet[0]);
    }

    /**
     * Returns the list of statues on home timeline from <code>first</code> to 
     * <code>pageSize</code>. The <code>statuses/home_timeline</code> can return 
     * up to a maximum of 800 statuses. The statuses are fetched during the first access.
     * A fresh fetch can be requested by sending "-1" for <code>first</code> parameter.
     * 
     * @param first Index of the first status
     * @param pageSize Total number of statuses to be returned
     * @return List of statuses
     */
    public Tweet[] getHomeTimeline(int first, int pageSize) {
        logger.log(Level.FINE, "getHomeTimeline({0}, {1})",
                new Object[] { first, pageSize });
        if (first == -1 || homeTimeline == null) {
            homeTimeline = getHomeTimeline();
        }

        return Arrays.copyOfRange(homeTimeline, first, first + pageSize);
    }
    
    private OAuthClientFilter getOAuthFilter() {
        logger.log(Level.FINE, "getOAuthFilter()");
        OAuthParameters params = new OAuthParameters().
                consumerKey(CONSUMER_KEY).
                token(oauth_token);
        OAuthSecrets secrets = new OAuthSecrets().
                consumerSecret(CONSUMER_SECRET).
                tokenSecret(oauth_token_secret);
        OAuthClientFilter oauthFilter = new OAuthClientFilter(
                client.getProviders(), 
                params, 
                secrets);
        return oauthFilter;
    }

    private <T> T getResource(String resource, Class clazz) {
        logger.log(Level.FINE, "getResource({0}, {1})",
                new Object[] { resource, clazz });
        WebResource webResource = client.resource(API_URI).path(resource);
        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        return (T)webResource.get(clazz);
    }
    
    private <T> T[] getResourceArray(String resource, Class clazz) {
        logger.log(Level.FINE, "getResourceArray({0}, {1})", 
                new Object[]{resource, clazz});
        WebResource webResource = client.resource(API_URI).path(resource);
        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        return (T[])webResource.get(clazz);
    }
    
    /**
     * Returns the list of followers from <code>first</code> to <code>pageSize</code>.
     * The maximum permitted <code>pageSize</code> is 100 as this is restricted
     * by the REST APIs of Twitter. The followers are fetched during the first access.
     * A fresh fetch can be requested by sending "-1" for <code>first</code> parameter.
     * 
     * @param first Index of the first follower
     * @param pageSize Total number of followers to be returned
     * @return List of followers
     */
    public User[] getFollowers(int first, int pageSize) {
        logger.log(Level.FINE, "getFollowers({0}, {1})",
                new Object[] { first, pageSize });
        if (first == -1 || followers == null) {
            WebResource webResource = client.resource(API_URI).path("/followers/ids.json");

            // Add filters to the resource
            if (logger.isLoggable(Level.FINE)) {
                webResource.addFilter(new LoggingFilter(logger));
            }
            webResource.addFilter(getOAuthFilter());
            followers = webResource.get(FriendsAndFollowers.class).getIds();
            logger.log(Level.FINE, "Initializing followers ...");
        }
        

        return getFF(followers, first, pageSize);
    }
    
    /**
     * Returns the list of friends from <code>first</code> to <code>pageSize</code>.
     * The maximum permitted <code>pageSize</code> is 100 as this is restricted
     * by the REST APIs of Twitter. The friends are fetched during the first access.
     * A fresh fetch can be requested by sending "-1" for <code>first</code> parameter.
     * 
     * @param first Index of the first friend
     * @param pageSize Total number of friends to be returned
     * @return List of friends
     */
    public User[] getFriends(int first, int pageSize) {
        logger.log(Level.FINE, "getFriends({0}, {1})", 
                new Object[] {first, pageSize});
        if (first == -1 || friends == null) {
            WebResource webResource = client.resource(API_URI).path("/friends/ids.json");

            // Add filters to the resource
            if (logger.isLoggable(Level.FINE)) {
                webResource.addFilter(new LoggingFilter(logger));
            }
            webResource.addFilter(getOAuthFilter());
            friends = webResource.get(FriendsAndFollowers.class).getIds();
            logger.log(Level.FINE, "Initializing friends ...");
        }
        

        return getFF(friends, first, pageSize);
    }
    
    private User[] getFF(long[] ff, int first, int pageSize) {
        logger.log(Level.FINE, "getFF({0}, {1}, {2})",
                new Object[]{ ff, first, pageSize});
        if (pageSize > 100) {
            logger.log(Level.FINE, "Specified page size is \"{0}\", defaulting to 100.", pageSize);
            pageSize = 100;
        }
        
        // a non-negative index will set it to the first one
        if (first < 0) {
            first = 0;
        }
        
        WebResource webResource2 = client.resource(API_URI).path("/users/lookup.json");
        if (logger.isLoggable(Level.FINE)) {
            webResource2.addFilter(new LoggingFilter(logger));
        }
        webResource2.addFilter(getOAuthFilter());
        
        StringBuilder ids = new StringBuilder();
        int endIndex = first + pageSize;
        if (ff.length < (first + pageSize)) {
            endIndex = ff.length;
        }
        logger.log(Level.FINE, "Returning followers/friends from {0} to {1}", 
                new Object[]{first, endIndex});
        for (int i=first; i<endIndex; i++) {
            if (ids.length() != 0) {
                ids.append(",");
            }
            ids.append(ff[i]);
        }
        webResource2 = webResource2.queryParam("user_id", ids.toString());
        User[] response = webResource2.get(User[].class);

        // Sort the response as users/lookup return random order
        Arrays.sort(response, new Comparator() {

            @Override
            public int compare(Object t, Object t1) {
                User u = (User)t;
                User u1 = (User)t1;
                return (int)(u.getId() - u1.getId());
            }
            
        });
        
        return response;
    }
            
    public User[] getFriends() {
        logger.log(Level.FINE, "getFriends()");
        return getFF("/friends/ids.json");
    }
    
    public User[] getFollowers() {
        logger.log(Level.FINE, "getFollowers()");
        return getFF("/followers/ids.json");
    }
    
    private User[] getFF(String resource) {
        logger.log(Level.FINE, "getFF({0})", resource);
        
        WebResource webResource = client.resource(API_URI).path(resource);
        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        FriendsAndFollowers ff = webResource.get(FriendsAndFollowers.class);
        
        WebResource webResource2 = client.resource(API_URI).path("/users/lookup.json");
        if (logger.isLoggable(Level.FINE)) {
            webResource2.addFilter(new LoggingFilter(logger));
        }
        webResource2.addFilter(getOAuthFilter());
        
        logger.log(Level.FINE, "Number of followers/friends: {0}", ff.getIds().length);

        List<User> list = new ArrayList<User>();
        StringBuilder ids = new StringBuilder();
        int totalLoops = (ff.getIds().length % 100 == 0) ? ff.getIds().length/100 : ff.getIds().length/100 + 1;
        for (int i=0; i < totalLoops; i++) {
            WebResource wr2 = client.resource(API_URI).path("/users/lookup.json");

            int startIndex = 100 * i;
            int endIndex = startIndex + 100;
            if (ff.getIds().length < startIndex + 100) {
                endIndex = ff.getIds().length;
            }
            logger.log(Level.FINE, "Processing {0} to {1} users", 
                    new Object[]{startIndex, endIndex});
            
            for (int j = startIndex; j < endIndex; j++) {
                if (ids.length() != 0) {
                    ids.append(",");
                }
                ids.append(ff.getIds()[j]);
            }
            wr2 = wr2.queryParam("user_id", ids.toString());
            list.addAll(Arrays.asList(wr2.get(User[].class)));
            ids.setLength(0);
        }
        return list.toArray(new User[0]);
    }
    
    public User[] getSuggestions() {
        logger.log(Level.FINE, "getSuggestions()");
        WebResource webResource = client.resource(API_URI).path("/users/suggestions.json");
        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());

        SuggestedUsersCategory[] res = webResource.get(SuggestedUsersCategory[].class);
        
        List<User> users = new ArrayList<User>();
        
        for (SuggestedUsersCategory f : res) {
            logger.log(Level.FINE, "Finding users for {0}", f.getSlug());
            WebResource webResource2 = client.resource(API_URI).path("/users/suggestions/" + f.getSlug() + ".json");
            if (logger.isLoggable(Level.FINE)) {
                webResource2.addFilter(new LoggingFilter(logger));
            }
            webResource.addFilter(getOAuthFilter());
            SuggestedUsers u = webResource2.get(SuggestedUsers.class);
            logger.log(Level.FINE, "Found {0}", u.getUsers().length);
            users.add(u.getUsers()[0]);
        }
        
        logger.log(Level.INFO, "Total users: {0}", users.size());
        logger.log(Level.FINE, users.toString());
        return users.toArray(new User[0]);
    }
    
    public Tweet[] getMentions() {
        logger.log(Level.FINE, "getMentions()");
        return getTweets("/statuses/mentions.json");
    }
    
    public DirectMessage[] getDirectMessages() {
        logger.log(Level.FINE, "getDirectMessages()");
        return (DirectMessage[])getResourceArray("/direct_messages.json", DirectMessage[].class);
    }
    
    private Tweet[] getTweets(String resource) {
        logger.log(Level.FINE, "getTweets({0})", resource);
        return getResourceArray(resource, Tweet[].class);
    }
    
    public Lists[] getLists() {
        logger.log(Level.FINE, "getLists()");
        return getResourceArray("/lists/all.json", Lists[].class);
    }
    
    public ListMemberships getListMemberships() {
        logger.log(Level.FINE, "getListMemberships()");
        return getResource("/lists/memberships.json", ListMemberships.class);
    }
    
    public Tweet[] getFavorites() {
        logger.log(Level.FINE, "getFavorites()");
        return getTweets("/favorites.json");
    }
    
    public List<Tweet> getUserTimeline2() {
        logger.log(Level.FINE, "getUserTimeline2()");
        WebResource webResource = client.resource("http://localhost:8080/WebApplication11/resources/generic/3");
//        WebResource webResource = client.resource("http://localhost:8080/WebApplication11/test.json");
        Tweet[] res = webResource.get(Tweet[].class);
        logger.log(Level.FINE, res.toString());
        
        return Arrays.asList(res);
        
//        return res.toString();
    }
    
    public void followTheUser(User u) {
        logger.log(Level.FINE, "getTweets({0})", u);
        // DO NOTHING FOR NOW
        // "following" API is broken and discussed at https://dev.twitter.com/discussions/5400
//        if (!u.isFollowing()) {
//            WebResource webResource = client.resource(API_URI).path("/friendships/create.json");
//            
//            webResource = webResource.queryParam("user_id", u.getId_str());
//            
//            // Add filters to the resource
//            webResource.addFilter(new LoggingFilter(logger));
//            webResource.addFilter(getOAuthFilter());
//            
//            webResource.post();
//        }
    }
    
    public void postTweet(String text) {
        logger.log(Level.FINE, "postTweet({0})", text);
        WebResource webResource = client.resource(API_URI).path("/statuses/update.json");
        
        // Add filters to the resource
        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        webResource.addFilter(getOAuthFilter());
        
//        webResource = webResource.queryParam("status", input.getTweetText());
        webResource = webResource.queryParam("status", text);
        
        webResource.post();
        
        // clearing up the text
//        input.setTweetText("");
        
    }
    
    /**
     * Unauthenticated search results beginning at page 1 and 20 results in the page.
     * 
     * @param searchString String to be searched
     * @return Results of search
     */
    public <T> T search(String searchString, Class clazz) {
        logger.log(Level.FINE, "getTweets({0}, {1})", 
                new Object[] { searchString, clazz });
        return (T)search(searchString, 1, 20, clazz);
    }
    
//    public String searchJSON(String searchString) {
//        return searchJSON(searchString, 1, 20);
//    }
    
    /**
     * Unauethenticated search results
     * 
     * @param searchString String to be searched
     * @param page Page number to return (starting at 1)
     * @param rpp Number of tweets to return per page
     * @return Results of search
     */
    public <T> T search(String searchString, int page, int rpp, Class clazz) {
        logger.log(Level.FINE, "getTweets({0}, {1})", 
                new Object[] { searchString, clazz });
        
        if (rpp < 0 || rpp > 100) {
            rpp = 100;
        }
        
        if (page < 1 || page > rpp) {
            page = 1;
        }
        
        WebResource webResource;
        try {
            webResource = client.resource(SEARCH_URI);
            webResource = webResource.queryParam("q", URLEncoder.encode(searchString, "UTF-8"))
                    .queryParam("page", String.valueOf(page))
                    .queryParam("rpp", String.valueOf(rpp))
                    .queryParam("result_type", "mixed");
            
            if (logger.isLoggable(Level.FINE)) {
                webResource.addFilter(new LoggingFilter(logger));
            }
            return (T)webResource.get(clazz);
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
//    /**
//     * Can use search and then convert SearchResults to JSON. But this will 
//     * provide one less layer of conversion.
//     * 
//     * @param searchString
//     * @param page
//     * @param rpp
//     * @return 
//     */
//    public String searchJSON(String searchString, int page, int rpp) {
//        if (rpp < 0 || rpp > 100)
//            rpp = 100;
//        
//        if (page < 1 || page > rpp)
//            page = 1;
//        
//        WebResource webResource;
//        try {
//            webResource = client.resource(SEARCH_URI);
////            webResource.accept(MediaType.APPLICATION_JSON);
//            webResource = webResource.queryParam("q", URLEncoder.encode(searchString, "UTF-8"))
//                    .queryParam("page", String.valueOf(page))
//                    .queryParam("rpp", String.valueOf(rpp));
//            
////            webResource.addFilter(new LoggingFilter(logger));
//            return webResource.get(String.class);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return null;
//    }
    
    public Trends[] getTrends(String woeid) {
        logger.log(Level.FINE, "getTrends({0})", woeid);
        WebResource webResource;
        webResource = client.resource(API_URI).path("/trends/" + woeid + ".json");

        if (logger.isLoggable(Level.FINE)) {
            webResource.addFilter(new LoggingFilter(logger));
        }
        return webResource.get(Trends[].class);
    }
}
