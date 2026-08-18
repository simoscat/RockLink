package dao.musician;

import dao.factories.DAOFactory;
import dao.instrument.InstrumentDAO;
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
    public Musician retrieveMusicianByEmail(String email) {

        if (!email.equals(mail)) {
            throw new DAOException("No musician with this email exists");
        }


        if (this.musician == null) {

            InstrumentDAO instrumentDAO = DAOFactory.getInstance().getInstrumentDAO();

            List<Instrument> iL = instrumentDAO.getMusicianInstruments("anna.muscatello@gmail.com");

            this.musician = new Musician("Anna", "Muscatello", "Muschio", "anna.muscatello@gmail.com",
                    Gender.FEMALE, iL);
        }

        return this.musician;
    }


    @Override
    protected void saveToPersistency(Musician m) {
        //nothing to do
    }


}
