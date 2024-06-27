package education.currencyexchange.utils;

public class DBCredentials {
    private final static String dbUser = "postgres";
    private final static String dbPassword = "pass";

    public static String getDBUser() {
        return dbUser;
    }

    public static String getDBPassword() {
        return dbPassword;
    }
}
