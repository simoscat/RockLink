package model;

import engineering.enums.CurrencyType;

import java.math.BigDecimal;

public class MoneyValue {
    private BigDecimal value; // not float because of rounding errors (e.g., 0.1 + 0.2 != 0.3)
    private CurrencyType currency;

    public MoneyValue(BigDecimal value, CurrencyType currency) {
        if (value.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Money value cannot be negative");
        }

        this.value = value;
        this.currency = currency;
    }

    public BigDecimal moneyAmount() {
        return value;
    }

    public String toString(){
        return value.toString() + " " + currency.name();
    }

    public void changeValue(BigDecimal newValue){
        this.value = newValue;
    }

    public void changeCurrency(CurrencyType newCurrency){
        this.currency = newCurrency;
    }

    public CurrencyType whichCurrency(){
        return this.currency;
    }
}
