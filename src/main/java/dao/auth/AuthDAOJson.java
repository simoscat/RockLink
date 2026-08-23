package dao.auth;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

import engineering.persistency.JsonManager;
import org.json.JSONArray;
import org.json.JSONObject;

import exception.DAOException;
import model.Credential;


public class AuthDAOJson extends AuthDAO {

    private final String musiciansFile;
    private final String promotersFile;

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "cryptPassword";


    public AuthDAOJson() {

        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);

            String basepath = prop.getProperty("json.path");

            musiciansFile = basepath+"musicians_creds.json";
            promotersFile = basepath+"promoters_creds.json";

        } catch (FileNotFoundException e) {
            throw new DAOException("Property file doesn't exist", e);
        } catch (IOException e) {
            throw new DAOException("Error reading properties file", e);
        }

    }

    @Override
    public Credential getMusicianCredential(String email) throws DAOException {
        Credential creds = getUserCredential(email, musiciansFile);

        if (creds == null){
            throw new DAOException("No musician credentials found for this email: " + email);
        }

        return creds;
    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        Credential creds = getUserCredential(email, promotersFile);

        if (creds == null){
            throw new DAOException("No promoter credentials found for this email: " + email);
        }

        return creds;
    }

    private Credential getUserCredential(String email, String path){

        JSONArray credentials = readCredentialsFile(path);

        for (int i = 0; i < credentials.length(); i++) {
            JSONObject obj = credentials.getJSONObject(i);
            if (obj.getString(EMAIL_KEY).equalsIgnoreCase(email)) {
                return new Credential(obj.getString(EMAIL_KEY), obj.getString(PASSWORD_KEY));
            }
        }

        return null;

    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        JSONArray credentials = readCredentialsFile(musiciansFile);

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

        writeCredentialsFile(musiciansFile, credentials);
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        JSONArray credentials = readCredentialsFile(promotersFile);

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

        writeCredentialsFile(promotersFile, credentials);
    }

    @Override
    public boolean isMusicianAlreadyRegistered(String email) {
        return getUserCredential(email, musiciansFile) != null;
    }

    @Override
    public boolean isPromoterAlreadyRegistered(String email) {
        return getUserCredential(email, promotersFile) != null;
    }

    private JSONArray readCredentialsFile(String path) throws DAOException {
        try {
            return JsonManager.readJsonFile(path);
        } catch (IOException e) {
            throw new DAOException("Couldn't read Json file "+path, e);
        }
    }

    private void writeCredentialsFile(String path, JSONArray credentials) throws DAOException {
        try{
            JsonManager.writeJsonFile(credentials, path);
        } catch(IOException e) {
            throw new DAOException("Couldn't write Json file "+path, e);
        }
    }

    
}
