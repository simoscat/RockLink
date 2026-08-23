package dao.auth;

import engineering.persistency.ConfigManager;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Credential;

import java.io.IOException;
import java.util.List;

public class AuthDAOCsv extends AuthDAO {

    private final String musiciansPath;
    private final String promotersPath;
    private static final String CSV_SEPARATOR = ",";
    private static final int NUM_FIELDS = 2;

    /*
    structure:
    email,cryptPassword
     */

    public AuthDAOCsv() {

        musiciansPath = ConfigManager.getProperty("csv.path") + "musicians_creds.csv";
        promotersPath = ConfigManager.getProperty("csv.path") + "promoters_creds.csv";

        try {
            CsvManager.initCsvFile(musiciansPath);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + musiciansPath, e);
        }

        try {
            CsvManager.initCsvFile(promotersPath);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + promotersPath, e);
        }

    }
    @Override
    public Credential getMusicianCredential(String email) throws DAOException {

        if (getUserCredential(email, musiciansPath) != null) {
            return getUserCredential(email, musiciansPath);
        }

        throw new DAOException("Couldn't find musician credentials: " + email);

    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        if (getUserCredential(email, promotersPath) != null) {
            return getUserCredential(email, promotersPath);
        }

        throw new DAOException("Couldn't find promoter credentials: " + email);
    }
    
    private Credential getUserCredential(String email, String path){
        String[] fields = CsvManager.findRow(path, f -> f.length >= NUM_FIELDS && f[0].equals(email));

        if (fields == null) {
            return null;
        }

        return toCredential(fields[0], fields[1]);
    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {
        registerUser(credential, musiciansPath);
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {
        registerUser(credential, promotersPath);
    }

    @Override
    public boolean isMusicianAlreadyRegistered(String email) {

        return getUserCredential(email, musiciansPath) != null;

    }

    @Override
    public boolean isPromoterAlreadyRegistered(String email) {

        return getUserCredential(email, promotersPath) != null;

    }

    private void registerUser(Credential credential, String path) {
        if (CsvManager.findRow(path, f -> f[0].equals(credential.getEmail())) != null) {
            throw new DAOException("Credential for email " + credential.getEmail() + " already exists in " + path);
        }

        List<String> lines = CsvManager.readAllLines(path);
        lines.add(toCsv(credential));
        CsvManager.writeAllLines(path, lines);
    }

    private String toCsv(Credential credential) {
        return String.join(CSV_SEPARATOR,
                credential.getEmail(),
                credential.getCryptPassword());
    }

    private Credential toCredential(String email, String cryptPassword) {
        return new Credential(email, cryptPassword);
    }

}
