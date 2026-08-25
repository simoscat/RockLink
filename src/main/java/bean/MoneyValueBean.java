package bean;

import java.math.BigDecimal;

public class MoneyValueBean {

    private String currency;
    private BigDecimal value;

    public MoneyValueBean(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
