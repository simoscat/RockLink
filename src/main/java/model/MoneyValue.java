package model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class MoneyValue {
    private final BigDecimal value; // not float because of rounding errors (e.g., 0.1 + 0.2 != 0.3)
    private final CurrencyEnum currency;

    public MoneyValue(BigDecimal value, CurrencyEnum currency) {
        if (value.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Money value cannot be negative");
        }

        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String toString(){
        return value.toString() + " " + currency.name();
    }

}
