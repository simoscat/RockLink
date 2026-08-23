package dao.instrument;

import engineering.persistency.ConfigManager;
import engineering.enums.Mastery;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.Instrument;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstrumentDAOJson extends InstrumentDAO {

    private final String path;
    private static final String EMAIL_FIELD = "email";
    private static final String INSTRUMENTS_FIELD = "instruments";

    public InstrumentDAOJson() {
        path = ConfigManager.getProperty("json.path") + "musicians_instruments.json";
    }

    @Override
    public List<Instrument> getMusicianInstruments(String musicianEmail) {
        JSONArray allRecords = JsonManager.readJsonFile(this.path);
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
        JSONObject newRecord = new JSONObject();
        newRecord.put(EMAIL_FIELD, musicianEmail);
        newRecord.put(INSTRUMENTS_FIELD, toJsonArray(instruments));

        JsonManager.upsertFile(this.path, obj -> obj.getString(EMAIL_FIELD).equals(musicianEmail), newRecord);
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
}
