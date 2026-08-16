package dao.musician;

import engineering.enums.Gender;
import engineering.enums.Mastery;
import exception.DAOException;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class MusicianDAODemo extends MusicianDAO{

    private Musician musician;
    private final String mail = "anna.muscatello@gmail.com";

    @Override
    public Musician getMusicianByEmail(String email) {

        if (!email.equals(mail)) {
            throw new DAOException("No musician with this email exists");
        }


        if (this.musician == null) {
            List<Instrument> iL = new ArrayList<>();

            Instrument c = new Instrument("Chitarra elettrica", Mastery.MASTER);

            Instrument c1 = new Instrument("Chitarra acustica", Mastery.EXPERIENCED);

            Instrument b = new Instrument("Batteria", Mastery.BEGINNER);

            iL.add(c);
            iL.add(c1);
            iL.add(b);

            this.musician = new Musician("Anna", "Muscatello", "Muschio", "anna.muscatello@gmail.com",
                    Gender.FEMALE, iL);
        }

        return this.musician;
    }

    @Override
    public void flush(Musician m) {
        //nothing to do in demo version
    }
}
