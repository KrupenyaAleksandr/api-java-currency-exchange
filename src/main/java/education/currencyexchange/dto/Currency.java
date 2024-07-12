package education.currencyexchange.dto;

import java.util.Objects;

public class Currency {

    private Integer id;
    private String code;
    private String name;
    private String sign;

    public Currency() {};

    public Currency(Integer id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
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
                && Objects.equals(name, currency.name)
                && Objects.equals(sign, currency.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, sign);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{\n");
        stringBuilder
                .append("\"id\": ")
                .append(this.id)
                .append(",\n")
                .append("\"name\": ")
                .append(this.name)
                .append(",\n")
                .append("\"code\": ")
                .append(this.code)
                .append(",\n")
                .append("\"sign\": ")
                .append(this.sign)
                .append("\n}");
        return stringBuilder.toString();
    }
}