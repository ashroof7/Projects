package queryProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Authenticator service, that checks user logins
 */
public class Authenticator {
    private static final Map<String, String> USERS = new HashMap<String, String>();
    static {
        USERS.put("demo", "demo");
        USERS.put("leonardo", "leonardo");
    }
    public static boolean validate(String user, String password){
        String validUserPassword = USERS.get(user);
        return validUserPassword != null && validUserPassword.equals(password);
    }
}
