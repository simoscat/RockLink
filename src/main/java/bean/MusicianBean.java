package bean;

import java.util.List;

public class MusicianBean extends UserBean {

    private String stageName;
    private List<InstrumentBean> instruments;

    public MusicianBean(String name, String surname, String email, String gender, String password,
                           String stageName, List<InstrumentBean> instruments) {
        super(name, surname, email, gender, password);
        this.stageName = stageName;
        this.instruments = instruments;
    }

    public MusicianBean(String email, String password) {
        super(email, password);
    }

    public void setStageName(String stageName){
        this.stageName = stageName;
    }

    public String getStageName(){
        return this.stageName;
    }

    public void setInstruments(List<InstrumentBean> instruments){
        this.instruments = instruments;
    }

    public List<InstrumentBean> getInstruments(){
        return this.instruments;
    }
}
