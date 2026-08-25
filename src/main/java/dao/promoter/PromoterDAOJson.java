package dao.promoter;

import engineering.persistency.ConfigManager;
import engineering.enums.Gender;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.Promoter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PromoterDAOJson extends PromoterDAO {

    private final String path;
    private static final String EMAIL_FIELD = "email";
    private static final String CONTACTS_FIELD = "contacts";

    public PromoterDAOJson() {
        path = ConfigManager.getProperty("json.path") + "promoters.json";
    }

    @Override
    protected Promoter retrievePromoterByEmail(String email) {
        JSONArray allRecords = JsonManager.readJsonFile(this.path);
        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(email)) {
                return parseJson(obj);
            }
        }
        throw new DAOException("No promoter found with email: " + email);
    }

    @Override
    protected void saveToPersistency(Promoter promoter) {
        JSONArray allRecords = JsonManager.readJsonFile(this.path);
        boolean found = false;

        for (int i = 0; i < allRecords.length(); i++) {
            JSONObject obj = allRecords.getJSONObject(i);
            if (obj.getString(EMAIL_FIELD).equals(promoter.getEmail())) {
                allRecords.put(i, toJson(promoter));
                found = true;
                break;
            }
        }

        if (!found) {
            allRecords.put(toJson(promoter));
        }

        JsonManager.writeJsonFile(allRecords, this.path);
    }

    private Promoter parseJson(JSONObject obj) {
        String email = obj.getString(EMAIL_FIELD);
        String name = obj.getString("name");
        String surname = obj.getString("surname");
        Gender gender = Gender.valueOf(obj.getString("gender"));
        
        Map<String, String> contactsMap = new HashMap<>();
        if (obj.has(CONTACTS_FIELD)) {
            JSONObject contactsObj = obj.getJSONObject(CONTACTS_FIELD);
            for (String key : contactsObj.keySet()) {
                contactsMap.put(key, contactsObj.getString(key));
            }
        }

        return new Promoter(name, surname, email, gender, contactsMap);
    }

    private JSONObject toJson(Promoter p) {
        JSONObject obj = new JSONObject();
        obj.put(EMAIL_FIELD, p.getEmail());
        obj.put("name", p.getName());
        obj.put("surname", p.getSurname());
        obj.put("gender", p.getGender().name());

        JSONObject contactsObj = new JSONObject();
        if (p.howToContact() != null) {
            for (Map.Entry<String, String> entry : p.howToContact().entrySet()) {
                contactsObj.put(entry.getKey(), entry.getValue());
            }
        }
        obj.put(CONTACTS_FIELD, contactsObj);

        return obj;
    }
}
