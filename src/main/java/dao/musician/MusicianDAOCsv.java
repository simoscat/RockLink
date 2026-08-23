package dao.musician;

import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOCsv;
import engineering.persistency.ConfigManager;
import engineering.persistency.CsvManager;
import engineering.enums.Gender;
import exception.DAOException;
import model.Instrument;
import model.Musician;

import java.io.IOException;
import java.util.List;

public class MusicianDAOCsv extends MusicianDAO {

    /*
    Structure:
    email,name,surname,stageName,gender
     */

    private static final String CSV_SEPARATOR = ",";

    private final String path;
    private final InstrumentDAO instrumentDAO;
    private static final int NUM_FIELDS = 5;


    public MusicianDAOCsv() {

        path = ConfigManager.getProperty("csv.path") + "musicians.csv";

        this.instrumentDAO = new InstrumentDAOCsv();

        try {
            CsvManager.initCsvFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.path, e);
        }

    }


    @Override
    public Musician retrieveMusicianByEmail(String email) {
        String[] fields = CsvManager.findRow(this.path, f -> f.length >= NUM_FIELDS && f[0].equals(email));

        if (fields == null) {
            throw new DAOException("Couldn't find musician with email: " + email);
        }

        return parseRow(fields);
    }

    @Override
    protected void saveToPersistency(Musician m) {
        CsvManager.upsertRow(this.path, fields -> fields[0].equals(m.getEmail()), toCsvRow(m));

        // gli strumenti sono gestiti dall'InstrumentDAO, non da questa classe
        this.instrumentDAO.saveMusicianInstruments(m.getEmail(), m.presentInstruments());
    }

    private Musician parseRow(String[] fields) {
        try {
            String csvEmail = fields[0];
            String name = fields[1];
            String surname = fields[2];
            String stageName = fields[3].replace("%2C", ",");
            Gender gender = Gender.valueOf(fields[4]);

            List<Instrument> instruments = this.instrumentDAO.getMusicianInstruments(csvEmail);

            return new Musician(name, surname, stageName, csvEmail, gender, instruments);

        } catch (IllegalArgumentException e) {
            // riga malformata (es. valore di Gender non valido)
            throw new DAOException("Invalid csv line for email " + fields[0], e);
        }
    }

    private String toCsvRow(Musician m) {
        return String.join(CSV_SEPARATOR,
                m.getEmail(),
                m.getName(),
                m.getSurname(),
                m.getArtistName().replace(",", "%2C"),
                m.getGender().name());
    }


}