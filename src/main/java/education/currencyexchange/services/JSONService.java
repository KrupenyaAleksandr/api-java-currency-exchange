package education.currencyexchange.services;

import education.currencyexchange.db.DatabaseEngine;
import education.currencyexchange.dto.Currency;

import java.sql.SQLException;
import java.util.List;


public class JSONService {

    public String getAllCurrenciesJSON() {
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
    }

}