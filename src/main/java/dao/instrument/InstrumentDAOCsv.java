package dao.instrument;

import engineering.persistency.ConfigManager;
import engineering.persistency.CsvManager;
import engineering.enums.Mastery;
import exception.DAOException;
import model.Instrument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstrumentDAOCsv extends InstrumentDAO {

    /*
    structure:
    email,instrument1;mastery1,instrument2;mastery2,instrument3;mastery3,...,instrumentN;masteryN

     */

    private final String path;
    private static final String CSV_SEPARATOR = ",";
    private static final String MASTERY_SEPARATOR = ";";

    public InstrumentDAOCsv() {

        path = ConfigManager.getProperty("csv.path") + "musicians_instruments.csv";

        try {
            CsvManager.initCsvFile(path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + path, e);
        }

    }


    @Override
    public List<Instrument> getMusicianInstruments(String musicianEmail) {
        String[] fields = CsvManager.findRow(path, f -> f[0].equals(musicianEmail));

        if (fields == null) {
            throw new DAOException("No instruments found for musician: " + musicianEmail);
        }

        return parseInstruments(fields);
    }

    private List<Instrument> parseInstruments(String[] fields) {

        try {
            List<Instrument> instruments = new ArrayList<>();

            for (int i = 1;  i < fields.length; i++) {

                String[] subfields = fields[i].split(MASTERY_SEPARATOR, -1);

                String name = subfields[0].replace("%2C", ",");

                Mastery mastery = Mastery.valueOf(subfields[1]);

                Instrument instr = new Instrument(name, mastery);

                instruments.add(instr);

            }

            return instruments;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new DAOException("Invalid instrument in line for email " + fields[0], e);
        }

    }

    @Override
    public void saveMusicianInstruments(String musicianEmail, List<Instrument> instruments) {
        CsvManager.upsertRow(path, fields -> fields[0].equals(musicianEmail), toCsv(musicianEmail, instruments));
    }

    private String toCsv(String email, List<Instrument> instruments) {

        StringBuilder toRet = new StringBuilder(email);

        for  (Instrument i : instruments){

            toRet.append(CSV_SEPARATOR).append(i.getName().replace(",", "%2C"))
                    .append(MASTERY_SEPARATOR).append(i.getMastery().name());


        }

        return toRet.toString();
    }

}
