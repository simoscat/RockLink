package dao.musician;

import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAODemo;
import model.Gender;
import exception.DAOException;
import model.*;

import java.util.List;

public class MusicianDAODemo extends MusicianDAO{

    private Musician musician;
    private final InstrumentDAO instrumentDAO;
    private static final String MUSICIAN_EMAIL = "anna.muscatello@gmail.com";

    public MusicianDAODemo() {
        this.instrumentDAO = new InstrumentDAODemo();
    }

    @Override
    public Musician retrieveMusicianByEmail(String email) {

        if (!email.equals(MUSICIAN_EMAIL)) {
            throw new DAOException("No musician with this email exists");
        }


        if (this.musician == null) {

            List<Instrument> iL = this.instrumentDAO.getMusicianInstruments(MUSICIAN_EMAIL);

            this.musician = new Musician("Anna", "Muscatello", "Muschio", MUSICIAN_EMAIL,
                    Gender.FEMALE, iL);
        }

        return this.musician;
    }

    @Override
    public List<String> retrieveAllEmails() {
        return List.of(MUSICIAN_EMAIL);
    }


    @Override
    protected void saveToPersistency(Musician m) {
        //nothing to do
    }


}
