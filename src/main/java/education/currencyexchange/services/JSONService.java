package education.currencyexchange.services;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;

import java.util.List;

public class JSONService {

    public JSONService() {}

    public String getAllCurrenciesJSON(List<Currency> currenciesList) {
        StringBuilder stringBuilder = new StringBuilder("[\n");

        for (Currency c: currenciesList) {
            stringBuilder
                    .append(c.toString())
                    .append(",\n");
        }

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }

    public String getAllExchangeRatesJSON(List<ExchangeRate> exchangeRatesList) {
        StringBuilder stringBuilder = new StringBuilder("[\n");

        for (ExchangeRate er: exchangeRatesList) {
            stringBuilder
                    .append(er.toString())
                    .append(",\n");
        }

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}