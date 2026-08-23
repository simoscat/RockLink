package dao.musician;

import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOJson;
import engineering.persistency.ConfigManager;
import engineering.enums.Gender;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.Instrument;
import model.Musician;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MusicianDAOJson extends MusicianDAO {

    private final String path;
    private final InstrumentDAO instrumentDAO;
    private static final String EMAIL_FIELD = "email";

    public MusicianDAOJson() {
        path = ConfigManager.getProperty("json.path") + "musicians.json";
        this.instrumentDAO = new InstrumentDAOJson();
    }

    @Override
    public Musician retrieveMusicianByEmail(String email) {
        JSONArray allRecords = JsonManager.readJsonFile(this.path);
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
        JSONArray allRecords = JsonManager.readJsonFile(this.path);
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

        JsonManager.writeJsonFile(allRecords, this.path);

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
}
