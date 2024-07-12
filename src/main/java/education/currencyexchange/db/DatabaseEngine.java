package education.currencyexchange.db;

import education.currencyexchange.dto.Currency;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseEngine {

    private static DatabaseEngine instance;
    private final String URL = "jdbc:postgresql://localhost:5432/currency?currentSchema=currency_schema";
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

    public List<Currency> getAllCurencies() throws SQLException {
        List<Currency> resultList;
        try (Connection con = DriverManager.getConnection(URL, dbProperties)) {
            try (Statement st = con.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM currencies")) {
                    resultList = new ArrayList<>();
                    while (rs.next()) {
                        resultList.add(new Currency(Integer.valueOf(rs.getString(1)),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4)));
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    throw new SQLException("Create result set error");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw  new SQLException("Create statement error");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Connection error");
        }
        return resultList;
    }
}