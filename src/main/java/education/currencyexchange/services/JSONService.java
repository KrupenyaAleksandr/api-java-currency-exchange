package education.currencyexchange.services;

import education.currencyexchange.db.DatabaseEngine;
import education.currencyexchange.dto.Currency;

import javax.ejb.Singleton;
import java.sql.SQLException;
import java.util.List;


@Singleton
public class JSONService {

    public JSONService() {}

/*    public String getAllCurrenciesJSON() {
        StringBuilder stringBuilder = new StringBuilder("[\n");
        try {
            List<Currency> currencies = DatabaseEngine.getInstance().getAllCurencies();
            for (Currency c: currencies) {
                stringBuilder
                        .append(c.toString())
                        .append(",\n");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "SQLException";
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("\n]");
        System.out.println("1");
        return stringBuilder.toString();
    }*/

    public String getAllCurrenciesJSON(List<Currency> currencies) {
        StringBuilder stringBuilder = new StringBuilder("[\n");
        for (Currency c: currencies) {
            stringBuilder
                    .append(c.toString())
                    .append(",\n");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }

}