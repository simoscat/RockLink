package model;

public class ElectricInstrumentDecorator extends InstrumentDecorator {

    public ElectricInstrumentDecorator(Instrument instrument) {
        super(instrument);
    }

    private String applyElectricDecorator(String s){
        return "Electric " + s;
    }

    @Override
    public String getName(){
        String oldName = super.getName();

        return this.applyElectricDecorator(oldName);
    }

}
