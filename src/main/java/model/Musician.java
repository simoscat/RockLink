package model;

import java.util.List;

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


}
