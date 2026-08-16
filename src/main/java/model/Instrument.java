package model;

import engineering.enums.Mastery;

public class Instrument {

    private String name;
    private Mastery mastery;

    public Instrument(String name, Mastery mastery) {
        this.name = name;
        this.mastery = mastery;
    }

    public String getName() {
        return this.name;
    }

    public Mastery getMastery() {
        return this.mastery;
    }

    public void setMastery(Mastery mastery) {
        this.mastery = mastery;
    }

    public void upMastery() {
        int currentNum = this.mastery.ordinal();

        this.mastery = Mastery.values()[(currentNum + 1) %  Mastery.values().length];
    }
}
