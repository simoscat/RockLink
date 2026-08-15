package model;

import engineering.enums.Mastery;

public interface Instrument {

    public String getName();

    public Mastery getMastery();

    public void setMastery(Mastery mastery);

    public void upMastery();

}
