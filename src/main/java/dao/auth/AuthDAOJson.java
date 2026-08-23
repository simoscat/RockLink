package dao.auth;

import engineering.persistency.ConfigManager;
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
        String basepath = ConfigManager.getProperty("json.path");

        musiciansFile = basepath+"musicians_creds.json";
        promotersFile = basepath+"promoters_creds.json";
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

        JSONObject obj = JsonManager.findInFile(path, o -> o.getString(EMAIL_KEY).equalsIgnoreCase(email));

        if (obj == null) {
            return null;
        }

        return new Credential(obj.getString(EMAIL_KEY), obj.getString(PASSWORD_KEY));
    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {
        registerCredential(credential, musiciansFile, "musician");
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {
        registerCredential(credential, promotersFile, "promoter");
    }

    private void registerCredential(Credential credential, String file, String role) {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        JSONArray credentials = JsonManager.readJsonFile(file);

        if (JsonManager.findFirst(credentials, o -> o.getString(EMAIL_KEY).equalsIgnoreCase(credential.getEmail())) != null) {
            throw new DAOException("A " + role + " credential with this email already exists: " + credential.getEmail());
        }

        JSONObject newCredential = new JSONObject();
        newCredential.put(EMAIL_KEY, credential.getEmail());
        newCredential.put(PASSWORD_KEY, credential.getCryptPassword());
        credentials.put(newCredential);

        JsonManager.writeJsonFile(credentials, file);
    }

    @Override
    public boolean isMusicianAlreadyRegistered(String email) {
        return getUserCredential(email, musiciansFile) != null;
    }

    @Override
    public boolean isPromoterAlreadyRegistered(String email) {
        return getUserCredential(email, promotersFile) != null;
    }
}
