package dao.instrument;

import engineering.ConfigManager;
import engineering.persistency.CsvManager;
import engineering.enums.Mastery;
import exception.DAOException;
import model.Instrument;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

        File file = new File(path);

        try(BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while((line = reader.readLine()) != null) {

                if (line.isBlank()){
                    continue;
                }

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields[0].equals(musicianEmail)) {
                    return parseInstruments(fields, line);
                }

            }

            throw new DAOException("No instruments found for musician: "+ musicianEmail);

        } catch (IOException e) {
            throw new DAOException("Couldn't read instrument file: ", e);
        }


    }

    private List<Instrument> parseInstruments(String[] fields, String line) {

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
            throw new DAOException("Invalid instrument in line: " + line, e);
        }

    }

    @Override
    public void saveMusicianInstruments(String musicianEmail, List<Instrument> instruments) {
        List<String> lines = readAllAndReplace(musicianEmail, instruments);

        File file = new File(path);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())){

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new DAOException("Couldn't write to instrument file: ", e);
        }
    }


    private List<String> readAllAndReplace(String email, List<Instrument> instruments) {

        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(path);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())){

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()){
                    continue;
                }

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields.length > 0 && fields[0].equals(email)){
                    lines.add(toCsv(email, instruments));
                    found = true;
                }
                else{
                    lines.add(line);
                }

            }

        } catch (IOException e) {
            throw new DAOException("Couldn't read instrument file: ", e);
        }

        if (!found){
            lines.add(toCsv(email, instruments));
        }

        return lines;

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
