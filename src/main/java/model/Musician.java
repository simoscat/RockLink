package model;

import engineering.enums.ArtistType;
import engineering.enums.Gender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Musician extends User implements Artist {
    private String stageName;
    private List<Instrument> instruments;

    public Musician(String name, String surname, String stageName, String email, Gender gender,
                    List<Instrument> instruments) {
        super(name, surname, email, gender);
        this.stageName = stageName;
        this.instruments = instruments;
    }

    @Override
    public String getArtistName() {
        return this.stageName;
    }

    @Override
    public ArtistType getType() {
        return ArtistType.MUSICIAN;
    }

    @Override
    public Map<String, String> getArtistDetails() {

        //TODO ATTENZIONE! NON SO SE STA COSA SI PUÒ FARE O SE DOVREBBE ESSERE DELEGATA ALLA VIEW, MA NON MI VIENE IN MENTE ALTRO MODO!!!!

        Map<String, String> artistDetails = new HashMap<>();

        artistDetails.put("Name", this.getName());
        artistDetails.put("Surname", this.getSurname());
        artistDetails.put("Stage name", this.stageName);
        artistDetails.put("Email", this.getEmail());
        artistDetails.put("Gender", this.getGender().toString());

        for (int i = 0; i < this.instruments.size(); i++) {
            artistDetails.put("Instrument " + (i + 1), this.instruments.get(i).getName());
            artistDetails.put(this.instruments.get(i).getName()+" Mastery", this.instruments.get(i).getMastery().name());
        }

        return artistDetails;

    }

    public List<Instrument> presentInstruments() {
        return Collections.unmodifiableList(instruments);
    }

    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
    }

}
