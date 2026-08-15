package dao.auth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.DAOException;
import model.Credential;


public class AuthDAOJson extends AuthDAO {

    private static final String MUSICIANS_FILE = "data/musicians_creds.json";
    private static final String PROMOTERS_FILE = "data/promoters_creds.json";

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "cryptPassword";




    @Override
    public Credential getMusicianCredential(String email) throws DAOException {
        JSONArray credentials = readCredentialsFile(MUSICIANS_FILE);

        for (int i = 0; i < credentials.length(); i++) {
            JSONObject obj = credentials.getJSONObject(i);
            if (obj.getString(EMAIL_KEY).equalsIgnoreCase(email)) {
                return new Credential(obj.getString(EMAIL_KEY), obj.getString(PASSWORD_KEY));
            }
        }

        throw new DAOException("No musician credentials found for this email: " + email);
    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        JSONArray credentials = readCredentialsFile(PROMOTERS_FILE);

        for (int i = 0; i < credentials.length(); i++) {
            JSONObject obj = credentials.getJSONObject(i);
            if (obj.getString(EMAIL_KEY).equalsIgnoreCase(email)) {
                return new Credential(obj.getString(EMAIL_KEY), obj.getString(PASSWORD_KEY));
            }
        }

        throw new DAOException("No promoter credentials found for this email: " + email);
    }


    @Override
    public void registerMusician(Credential credential) throws DAOException {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        JSONArray credentials = readCredentialsFile(MUSICIANS_FILE);

        for (int i = 0; i < credentials.length(); i++) {
            JSONObject obj = credentials.getJSONObject(i);
            if (obj.getString(EMAIL_KEY).equalsIgnoreCase(credential.getEmail())) {
                throw new DAOException("A musician credential with this email already exists: " + credential.getEmail());
            }
        }

        JSONObject newCredential = new JSONObject();
        newCredential.put(EMAIL_KEY, credential.getEmail());
        newCredential.put(PASSWORD_KEY, credential.getCryptPassword());
        credentials.put(newCredential);

        writeCredentialsFile(MUSICIANS_FILE, credentials);
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        JSONArray credentials = readCredentialsFile(PROMOTERS_FILE);

        for (int i = 0; i < credentials.length(); i++) {
            JSONObject obj = credentials.getJSONObject(i);
            if (obj.getString(EMAIL_KEY).equalsIgnoreCase(credential.getEmail())) {
                throw new DAOException("A promoter credential with this email already exists: " + credential.getEmail());
            }
        }

        JSONObject newCredential = new JSONObject();
        newCredential.put(EMAIL_KEY, credential.getEmail());
        newCredential.put(PASSWORD_KEY, credential.getCryptPassword());
        credentials.put(newCredential);

        writeCredentialsFile(PROMOTERS_FILE, credentials);
    }

    private JSONArray readCredentialsFile(String path) throws DAOException {
        File file = new File(path);

        if (!file.exists()) {
            return new JSONArray();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isBlank()) {
                return new JSONArray();
            }
            return new JSONArray(content);
        } catch (IOException e) {
            throw new DAOException("File reading error: " + file.getPath(), e);
        }
    }

    private void writeCredentialsFile(String path, JSONArray credentials) throws DAOException {
        File file = new File(path);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(credentials.toString(4));
        } catch (IOException e) {
            throw new DAOException("File writing error: " + file.getPath(), e);
        }
    }
    
}
