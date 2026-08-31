package model;

import engineering.enums.ArtistType;
import engineering.enums.Gender;

import java.util.*;

public class Musician extends User implements Artist {
    private String stageName;
    private List<Instrument> instruments;

    public Musician(String name, String surname, String stageName, String email, Gender gender,
                    List<Instrument> instruments) {
        super(name, surname, email, gender);
        this.stageName = stageName;

        this.instruments = new ArrayList<>();

        for (Instrument old : instruments) {

            Instrument instrument = new Instrument(
                    old.getName(),
                    old.getMastery()
            );

            this.instruments.add(instrument);

        }
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
    public LinkedHashMap<String, String> getArtistDetails() {

        LinkedHashMap<String, String> artistDetails = new LinkedHashMap<>();
        //we use this to then return the values in order

        artistDetails.put("Name", this.getName());
        artistDetails.put("Surname", this.getSurname());
        artistDetails.put("Stage name", this.stageName);
        artistDetails.put("Email", this.getEmail());
        artistDetails.put("Gender", this.getGender().toString().replace("_", " "));

        for (int i = 0; i < this.instruments.size(); i++) {
            artistDetails.put("Instrument " + (i + 1), this.instruments.get(i).getName() + " [" + this.instruments.get(i).getMastery().name() + "]");
        }

        return artistDetails;

    }

    public List<Instrument> presentInstruments() {

        List<Instrument> toRet = new ArrayList<>();

        for (Instrument instrument : this.instruments) {

            Instrument i = new Instrument(
                    instrument.getName(),
                    instrument.getMastery()
            );

            toRet.add(i);

        }

        return toRet;

    }

    public void addInstrument(Instrument instrument) {

        Instrument i = new Instrument(
                instrument.getName(),
                instrument.getMastery()
        );

        this.instruments.add(i);

    }

}
