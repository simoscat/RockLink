package bean;

import java.util.List;

public class MusicianBean extends UserBean {

    private String stageName;
    private List<String> instruments;

    public MusicianBean(String name, String surname, String email, String gender, String password,
                           String stageName, List<String> instruments) {
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

    public void setInstruments(List<String> instruments){
        this.instruments = instruments;
    }

    public List<String> getInstruments(){
        return this.instruments;
    }
}
