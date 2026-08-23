package dao.musician;

import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOJson;
import engineering.enums.Gender;
import exception.DAOException;
import model.Instrument;
import model.Musician;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

public class MusicianDAOJson extends MusicianDAO {

    private final String path;
    private final InstrumentDAO instrumentDAO;
    private static final String EMAIL_FIELD = "email";

    public MusicianDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            path = prop.getProperty("json.path") + "musicians.json";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        this.instrumentDAO = new InstrumentDAOJson();
    }

    @Override
    public Musician retrieveMusicianByEmail(String email) {
        JSONArray allRecords = readJsonFile();
        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(email)) {
                return parseJson(obj);
            }
        }
        throw new DAOException("Couldn't find musician with email: " + email);
    }

    @Override
    protected void saveToPersistency(Musician m) {
        JSONArray allRecords = readJsonFile();
        boolean found = false;

        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(m.getEmail())) {
                allRecords.put(i, toJson(m));
                found = true;
                break;
            }
        }

        if (!found) {
            allRecords.put(toJson(m));
        }

        writeJsonFile(allRecords);

        // Save instruments
        this.instrumentDAO.saveMusicianInstruments(m.getEmail(), m.presentInstruments());
    }

    private Musician parseJson(JSONObject obj) {
        String email = obj.getString(EMAIL_FIELD);
        String name = obj.getString("name");
        String surname = obj.getString("surname");
        String stageName = obj.getString("stageName");
        Gender gender = Gender.valueOf(obj.getString("gender"));

        List<Instrument> instruments;
        try {
            instruments = this.instrumentDAO.getMusicianInstruments(email);
        } catch (DAOException _) {
            throw new DAOException("Couldn't find musician with email " + email + " instruments");
        }

        return new Musician(name, surname, stageName, email, gender, instruments);
    }

    private JSONObject toJson(Musician m) {
        JSONObject obj = new JSONObject();
        obj.put(EMAIL_FIELD, m.getEmail());
        obj.put("name", m.getName());
        obj.put("surname", m.getSurname());
        obj.put("stageName", m.getArtistName());
        obj.put("gender", m.getGender().name());
        return obj;
    }

    private JSONArray readJsonFile() {
        File file = new File(this.path);
        if (!file.exists()) {
            return new JSONArray();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isBlank()) return new JSONArray();
            return new JSONArray(content);
        } catch (IOException e) {
            throw new DAOException("Error reading json file " + this.path, e);
        }
    }

    private void writeJsonFile(JSONArray array) {
        File file = new File(this.path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            throw new DAOException("Error writing json file " + this.path, e);
        }
    }
}
