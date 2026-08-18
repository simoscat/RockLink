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

    private final String PATH;

    public PromoterDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("json.path") + "promoters.json";
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
            if (obj.getString("email").equals(email)) {
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
            if (obj.getString("email").equals(promoter.getEmail())) {
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
        String email = obj.getString("email");
        String name = obj.getString("name");
        String surname = obj.getString("surname");
        Gender gender = Gender.valueOf(obj.getString("gender"));
        
        Map<String, String> contactsMap = new HashMap<>();
        if (obj.has("contacts")) {
            JSONObject contactsObj = obj.getJSONObject("contacts");
            for (String key : contactsObj.keySet()) {
                contactsMap.put(key, contactsObj.getString(key));
            }
        }

        return new Promoter(name, surname, email, gender, contactsMap);
    }

    private JSONObject toJson(Promoter p) {
        JSONObject obj = new JSONObject();
        obj.put("email", p.getEmail());
        obj.put("name", p.getName());
        obj.put("surname", p.getSurname());
        obj.put("gender", p.getGender().name());

        JSONObject contactsObj = new JSONObject();
        if (p.promoterContacts() != null) {
            for (Map.Entry<String, String> entry : p.promoterContacts().entrySet()) {
                contactsObj.put(entry.getKey(), entry.getValue());
            }
        }
        obj.put("contacts", contactsObj);

        return obj;
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
