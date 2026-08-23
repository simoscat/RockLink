package dao.musician;

import dao.factories.DAOFactory;
import dao.instrument.InstrumentDAO;
import engineering.enums.Gender;
import exception.DAOException;
import model.*;

import java.util.List;

public class MusicianDAODemo extends MusicianDAO{

    private Musician musician;
    private static final String MUSICIAN_EMAIL = "anna.muscatello@gmail.com";

    @Override
    public Musician retrieveMusicianByEmail(String email) {

        if (!email.equals(MUSICIAN_EMAIL)) {
            throw new DAOException("No musician with this email exists");
        }


        if (this.musician == null) {

            InstrumentDAO instrumentDAO = DAOFactory.getInstance().getInstrumentDAO();

            List<Instrument> iL = instrumentDAO.getMusicianInstruments(MUSICIAN_EMAIL);

            this.musician = new Musician("Anna", "Muscatello", "Muschio", MUSICIAN_EMAIL,
                    Gender.FEMALE, iL);
        }

        return this.musician;
    }


    @Override
    protected void saveToPersistency(Musician m) {
        //nothing to do
    }


}
