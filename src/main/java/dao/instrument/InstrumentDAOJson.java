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

//TODO CONTROLLA!!!!


public class InstrumentDAOJson extends InstrumentDAO {

    private final String PATH;

    public InstrumentDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("json.path") + "musicians_instruments.json";
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
            if (obj.getString("email").equals(musicianEmail)) {
                return parseInstruments(obj.getJSONArray("instruments"));
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
            if (obj.getString("email").equals(musicianEmail)) {
                obj.put("instruments", toJsonArray(instruments));
                found = true;
                break;
            }
        }

        if (!found) {
            JSONObject newRecord = new JSONObject();
            newRecord.put("email", musicianEmail);
            newRecord.put("instruments", toJsonArray(instruments));
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
        File file = new File(this.PATH);
        if (!file.exists()) {
            return new JSONArray();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isBlank()) return new JSONArray();
            return new JSONArray(content);
        } catch (IOException e) {
            throw new DAOException("Error reading json file " + this.PATH, e);
        }
    }

    private void writeJsonFile(JSONArray array) {
        File file = new File(this.PATH);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            throw new DAOException("Error writing json file " + this.PATH, e);
        }
    }
}
