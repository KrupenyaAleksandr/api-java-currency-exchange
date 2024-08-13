package education.currencyexchange.models;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ExchangeRate {
    private Long id;
    @NotNull
    private Currency baseCurrency;
    @NotNull
    private Currency targetCurrency;
    @NotNull
    private BigDecimal rate;

    public ExchangeRate() {}

    public ExchangeRate(ResultSet resultSet, Currency baseCurrency, Currency targetCurrency) throws SQLException {
        this.id = resultSet.getLong("id");
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = resultSet.getBigDecimal("rate");
    }

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Currency getTargetCurrency() {
        return this.targetCurrency;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(targetCurrency, that.targetCurrency) &&
                Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, targetCurrency, rate);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{\n");
        stringBuilder
                .append("\"id\": ")
                .append(this.id)
                .append(",\n")
                .append("\"baseCurrency\": ")
                .append(baseCurrency.toString())
                .append(",\n")
                .append("\"targetCurrency\": ")
                .append(targetCurrency.toString())
                .append(",\n")
                .append("\"rate\": ")
                .append(this.rate)
                .append("\n}");
        return stringBuilder.toString();
    }
}
