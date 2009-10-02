package samples.connectors.mailconnector.ra.inbound;

import org.glassfish.security.common.PrincipalImpl;

import javax.security.auth.Subject;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.resource.spi.work.SecurityContext;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.logging.Logger;
import javax.security.auth.message.callback.PasswordValidationCallback;

/**
 * @author jagadish
 */
public class MySecurityContext extends SecurityContext {
    private String userName;
    private String password;
    private String principalName;
    private Subject subject;

        static Logger logger =
        Logger.getLogger("samples.connectors.mailconnector.ra.inbound");

    public MySecurityContext(String userName, String password, String principalName){
        this.userName = userName;
        this.password = password;
        this.principalName = principalName;
        logger.fine("[MySecurityContext] constructor");
    }


    public void setupSecurityContext(CallbackHandler callbackHandler, Subject execSubject, Subject serviceSubject) {

        execSubject.getPrincipals().add(new PrincipalImpl(principalName));
        List<Callback> callbacks = new ArrayList<Callback>();

        CallerPrincipalCallback cpc = new CallerPrincipalCallback(execSubject, new PrincipalImpl(principalName));

        logger.fine("[SC] setting caller principal callback with principal : " + principalName);
        callbacks.add(cpc);

        PasswordValidationCallback pvc = null;
        pvc = new PasswordValidationCallback(execSubject, userName,
                password.toCharArray());
        logger.fine("[SC] setting password validation callback with user [ " + userName + " ] + password [ " + password + " ]");

        callbacks.add(pvc);

        Callback callbackArray[] = new Callback[callbacks.size()];
        try{
            callbackHandler.handle(callbacks.toArray(callbackArray));

        }catch(UnsupportedCallbackException e){
            debug("exception occured : " + e.getMessage());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
            debug("exception occured : " + e.getMessage());
        }


        if(!pvc.getResult()){
            logger.info("[SC] Password validation callback failure for user : " + userName);
            throw new IllegalStateException("User [ " + userName + " ] not authorized to send message");
        }else{
            logger.info("[SC] Password validation callback succeded for user : " + userName);
        }
    }

    public String toString(){
        StringBuffer toString = new StringBuffer("{");
        toString.append("userName : " + userName);
        toString.append(", password : " + password);
        toString.append(", principalName : " + principalName);
        toString.append("}");
        return toString.toString();
    }

    public void debug(String message){
        logger.fine("[SC]: " + message);
    }
}
