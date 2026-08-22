package dao.promoter;

import engineering.enums.Gender;
import exception.DAOException;
import model.Promoter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//TODO CONTROLLA!!!!

public class PromoterDAOJson extends PromoterDAO {

    private final String path;
    private static final String EMAIL_FIELD = "email";
    private static final String CONTACTS_FIELD = "contacts";

    public PromoterDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            path = prop.getProperty("json.path") + "promoters.json";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }
    }

    @Override
    protected Promoter retrievePromoterByEmail(String email) {
        JSONArray allRecords = readJsonFile();
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
        JSONArray allRecords = readJsonFile();
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

        writeJsonFile(allRecords);
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
        if (p.promoterContacts() != null) {
            for (Map.Entry<String, String> entry : p.promoterContacts().entrySet()) {
                contactsObj.put(entry.getKey(), entry.getValue());
            }
        }
        obj.put(CONTACTS_FIELD, contactsObj);

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
