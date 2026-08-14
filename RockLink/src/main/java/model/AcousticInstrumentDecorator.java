package model;

public class AcousticInstrumentDecorator extends InstrumentDecorator {

    public AcousticInstrumentDecorator(Instrument instrument) {
        super(instrument);
    }

    private String applyAcousticDecorator(String s){
        return "Acoustic " + s;
    }

    @Override
    public String getName(){
        String oldName = super.getName();

        return this.applyAcousticDecorator(oldName);
    }

}
