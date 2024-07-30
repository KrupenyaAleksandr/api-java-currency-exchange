package education.currencyexchange.services;

import education.currencyexchange.models.Currency;

import java.util.List;

public class JSONService {

    public JSONService() {}

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