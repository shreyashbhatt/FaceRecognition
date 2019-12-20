package Application;

public final class UserSession {

    private static UserSession instance;

    private static String email;

    private UserSession(String emailn) {
       email = emailn;
    }

    public static UserSession getInstace(String email) {
        if(instance == null) {
            instance = new UserSession(email);
        }
        return instance;
    }

    public static String getemail() {
        return email;
    }


    public static void cleanUserSession() {
        email = "";// or null
    }
}