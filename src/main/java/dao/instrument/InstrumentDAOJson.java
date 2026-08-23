package dao.instrument;

import engineering.enums.Mastery;
import exception.DAOException;
import model.Instrument;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InstrumentDAOJson extends InstrumentDAO {

    private final String path;
    private static final String EMAIL_FIELD = "email";
    private static final String INSTRUMENTS_FIELD = "instruments";

    public InstrumentDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            path = prop.getProperty("json.path") + "musicians_instruments.json";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }
    }

    @Override
    public List<Instrument> getMusicianInstruments(String musicianEmail) {
        JSONArray allRecords = readJsonFile();
        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(musicianEmail)) {
                return parseInstruments(obj.getJSONArray(INSTRUMENTS_FIELD));
            }
        }
        throw new DAOException("No instruments found for musician: " + musicianEmail);
    }

    @Override
    public void saveMusicianInstruments(String musicianEmail, List<Instrument> instruments) {
        JSONArray allRecords = readJsonFile();
        boolean found = false;

        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(musicianEmail)) {
                obj.put(INSTRUMENTS_FIELD, toJsonArray(instruments));
                found = true;
                break;
            }
        }

        if (!found) {
            JSONObject newRecord = new JSONObject();
            newRecord.put(EMAIL_FIELD, musicianEmail);
            newRecord.put(INSTRUMENTS_FIELD, toJsonArray(instruments));
            allRecords.put(newRecord);
        }

        writeJsonFile(allRecords);
    }

    private List<Instrument> parseInstruments(JSONArray jsonArray) {
        List<Instrument> instruments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String name = obj.getString("name");
            Mastery mastery = Mastery.valueOf(obj.getString("mastery"));
            instruments.add(new Instrument(name, mastery));
        }
        return instruments;
    }

    private JSONArray toJsonArray(List<Instrument> instruments) {
        JSONArray array = new JSONArray();
        for (Instrument i : instruments) {
            JSONObject obj = new JSONObject();
            obj.put("name", i.getName());
            obj.put("mastery", i.getMastery().name());
            array.put(obj);
        }
        return array;
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
