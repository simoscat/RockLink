package dao.musician;

import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOCsv;
import engineering.persistency.CsvManager;
import engineering.enums.Gender;
import exception.DAOException;
import model.Instrument;
import model.Musician;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

        try(InputStream is = new FileInputStream("config.properties")){

            Properties prop = new Properties();

            prop.load(is);

            path = prop.getProperty("csv.path") + "musicians.csv";

        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        this.instrumentDAO = new InstrumentDAOCsv();

        try {
            CsvManager.initCsvFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.path, e);
        }

    }


    @Override
    public Musician retrieveMusicianByEmail(String email) {
        File file = new File(this.path);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Musician candidate = parseRowIfMatches(line, email);
                if (candidate != null) {
                    return candidate;
                }
            }

            throw new DAOException("Couldn't find musician with email: " + email);

        } catch (IOException e) {
            throw new DAOException("Can't read musician with email: " + email, e);
        }
    }

    @Override
    protected void saveToPersistency(Musician m) {
        List<String> lines = readAllLinesReplacingMusician(m);

        File file = new File(this.path);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't save musician with email " + m.getEmail(), e);
        }

        // gli strumenti sono gestiti dall'InstrumentDAO, non da questa classe
        this.instrumentDAO.saveMusicianInstruments(m.getEmail(), m.presentInstruments());
    }

    private Musician parseRowIfMatches(String line, String email) {
        try {
            String[] fields = line.split(CSV_SEPARATOR, -1);

            if (fields.length < NUM_FIELDS || !fields[0].equals(email)) {
                return null;
            }

            String csvEmail = fields[0];
            String name = fields[1];
            String surname = fields[2];
            String stageName = fields[3];
            Gender gender = Gender.valueOf(fields[4]);

            List<Instrument> instruments = this.instrumentDAO.getMusicianInstruments(csvEmail);

            return new Musician(name, surname, stageName, csvEmail, gender, instruments);

        } catch (IllegalArgumentException e) {
            // riga malformata (es. valore di Gender non valido)
            throw new DAOException("Invalid csv line: " + line, e);
        }
    }

    private List<String> readAllLinesReplacingMusician(Musician m) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(this.path);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields.length > 0 && fields[0].equals(m.getEmail())) {
                    lines.add(toCsvRow(m));
                    found = true;
                } //if the musician is found we add this to the rows instead

                else { // if it wasn't, we add the current row
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + this.path, e);
        }

        if (!found) {
            lines.add(toCsvRow(m));
        } //if the musician is never found we add it to the bottom of the list


        return lines;
    }

    private String toCsvRow(Musician m) {
        return String.join(CSV_SEPARATOR,
                m.getEmail(),
                m.getName(),
                m.getSurname(),
                m.getArtistName(),
                m.getGender().name());
    }


}