package bean;

public class InstrumentBean {

    private String name;
    private String mastery;

    public InstrumentBean(String name, String mastery) {
        this.name = name;
        this.mastery = mastery;
    }

    public String getName() {
        return name;
    }

    public String getMastery() {
        return mastery;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMastery(String mastery) {
        this.mastery = mastery;
    }

}
