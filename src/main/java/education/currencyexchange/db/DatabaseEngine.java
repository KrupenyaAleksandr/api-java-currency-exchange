package education.currencyexchange.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseEngine {
    private static DatabaseEngine instance;
    private final String URL = "jdbc:postgresql://localhost:5432/currency";
    private Properties dbProperties;

    private DatabaseEngine() {
        try (InputStream input = DatabaseEngine.class.getClassLoader().getResourceAsStream("dbconfig.properties")){
            dbProperties = new Properties();
            dbProperties.load(input);
        }
        catch (IOException ex) {
            System.out.println("Error while getting properties");
        }
    }

    public static DatabaseEngine getInstance() {
        if (instance == null) {
            instance = new DatabaseEngine();
        }
        return instance;
    }

    public void test() {
        //TODO: fix connection error
        try (Connection con = DriverManager.getConnection(URL, dbProperties)) {
            System.out.println("1");
        }
        catch (SQLException ex) {
            System.out.println("Connection error");
        }
    }
}