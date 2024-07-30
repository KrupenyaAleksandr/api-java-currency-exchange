package education.currencyexchange.models;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class Currency {

    private Long id;
    @NotNull
    private String code;
    @NotNull
    private String fullName;
    @NotNull
    private String sign;

    public Currency() {};

    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(ResultSet resultSet) throws SQLException {
        try {
            this.id = resultSet.getLong("id");
            this.code = resultSet.getString("code");
            this.fullName = resultSet.getString("full_name");
            this.sign = resultSet.getString("sign");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Currency create SQLException");
        }
    }

    public Currency(HashMap<String, String> currencyMap) {
        this.code = currencyMap.get("code");
        this.fullName = currencyMap.get("name");
        this.sign = currencyMap.get("sign");
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id)
                && Objects.equals(code, currency.code)
                && Objects.equals(fullName, currency.fullName)
                && Objects.equals(sign, currency.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, fullName, sign);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{\n");
        stringBuilder
                .append("\"id\": ")
                .append(this.id)
                .append(",\n")
                .append("\"name\": \"")
                .append(this.fullName)
                .append("\",\n")
                .append("\"code\": \"")
                .append(this.code)
                .append("\",\n")
                .append("\"sign\": \"")
                .append(this.sign)
                .append("\"\n}");
        return stringBuilder.toString();
    }
}