package dao.instrument;

import engineering.CsvManager;
import engineering.enums.Mastery;
import exception.DAOException;
import model.Instrument;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InstrumentDAOCsv extends InstrumentDAO {

    /*
    structure:
    email,instrument1;mastery1,instrument2;mastery2,instrument3;mastery3,...,instrumentN;masteryN

     */

    private final String PATH;
    private static final String CSV_SEPARATOR = ",";
    private static final String MASTERY_SEPARATOR = ";";

    public InstrumentDAOCsv() {

        try(InputStream is = new FileInputStream("config.properties")){

            Properties prop = new Properties();

            prop.load(is);

            PATH = prop.getProperty("csv.path") + "musicians_instruments.csv";

        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        try {
            CsvManager.initCsvFile(PATH);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + PATH, e);
        }

    }


    @Override
    public List<Instrument> getMusicianInstruments(String musicianEmail) {

        File file = new File(PATH);

        try(BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while((line = reader.readLine()) != null) {

                if (line.isBlank()){
                    continue;
                }

                List<Instrument> instruments = parseRowIfMatches(line, musicianEmail);

                if (instruments != null){
                    return instruments;
                }

            }

            throw new DAOException("No instruments found for musician: "+ musicianEmail);

        } catch (IOException e) {
            throw new DAOException("Couldn't read instrument file: ", e);
        }


    }

    private List<Instrument> parseRowIfMatches(String line, String musicianEmail) {

        String[] fields = line.split(CSV_SEPARATOR, -1);

        if (!fields[0].equals(musicianEmail)){
            return null;
        }

        try {
            List<Instrument> instruments = new ArrayList<>();

            for (int i = 1;  i < fields.length; i++) {

                String[] subfields = fields[i].split(MASTERY_SEPARATOR, -1);

                String name = subfields[0];

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

        File file = new File(PATH);

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

        File file = new File(PATH);

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

            toRet.append(CSV_SEPARATOR).append(i.getName()).append(MASTERY_SEPARATOR).append(i.getMastery().name());


        }

        return toRet.toString();
    }

}
