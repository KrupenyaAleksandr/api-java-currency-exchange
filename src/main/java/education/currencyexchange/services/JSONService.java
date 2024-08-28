package education.currencyexchange.services;

import education.currencyexchange.models.Currency;
import education.currencyexchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class JSONService {

    public JSONService() {}

    public static String getAllCurrenciesJSON(List<Currency> currenciesList) {
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

    public static String getAllExchangeRatesJSON(List<ExchangeRate> exchangeRatesList) {
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

    public static String getExchangeJSON(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        StringBuilder stringBuilder = new StringBuilder(exchangeRate.toString().substring(0, exchangeRate.toString().length() - 2));

        stringBuilder
                .append(",\n")
                .append("\"amount\": ")
                .append(amount.toString())
                .append(",\n")
                .append("\"convertedAmount\": ")
                .append(convertedAmount.toString())
                .append("\n}");

        return stringBuilder.toString();
    }

    public static String getCrossExchangeJSON(ExchangeRate from, ExchangeRate to, BigDecimal amount, BigDecimal convertedAmount) {
        StringBuilder stringBuilder = new StringBuilder("{\n");

        stringBuilder
                .append(from.toString())
                .append(",\n")
                .append(to.toString())
                .append(",\n")
                .append("\"amount\": ")
                .append(amount.toString())
                .append(",\n")
                .append("\"convertedAmount\": ")
                .append(convertedAmount.toString())
                .append("\n}");

        return stringBuilder.toString();
    }
}