package model;

import engineering.enums.Mastery;

public class BaseInstrument implements Instrument {

    private String name;
    private Mastery mastery;

    public BaseInstrument(String name, Mastery mastery) {
        this.name = name;
        this.mastery = mastery;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Mastery getMastery() {
        return this.mastery;
    }

    @Override
    public void setMastery(Mastery mastery) {
        this.mastery = mastery;
    }

    @Override
    public void upMastery() {
        int currentNum = this.mastery.ordinal();

        this.mastery = Mastery.values()[(currentNum + 1) %  Mastery.values().length];
    }
}
