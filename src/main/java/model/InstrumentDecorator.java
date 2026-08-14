package model;

public abstract class InstrumentDecorator implements Instrument {

    Instrument wrappedInstrument;

    protected InstrumentDecorator(Instrument instrument) {
        this.wrappedInstrument = instrument;
    }

    @Override
    public String getName() {
        return this.wrappedInstrument.getName();
    }

    @Override
    public Mastery getMastery() {
        return this.wrappedInstrument.getMastery();
    }

    @Override
    public void setMastery(Mastery mastery) {
        this.wrappedInstrument.setMastery(mastery);
    }

    @Override
    public void upMastery() {
        this.wrappedInstrument.upMastery();
    }
}
