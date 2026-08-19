package dao.auth;

import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Credential;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AuthDAOCsv extends AuthDAO {

    private final String MUSICIANS_PATH;
    private final String PROMOTERS_PATH;
    private static final String CSV_SEPARATOR = ",";
    private static final int NUM_FIELDS = 2;

    /*
    structure:
    email,cryptPassword
     */

    public AuthDAOCsv() {

        try(InputStream is = new FileInputStream("config.properties")){

            Properties prop = new Properties();

            prop.load(is);

            MUSICIANS_PATH = prop.getProperty("csv.path") + "musicians_creds.csv";
            PROMOTERS_PATH = prop.getProperty("csv.path") + "promoters_creds.csv";

        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        try {
            CsvManager.initCsvFile(MUSICIANS_PATH);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + MUSICIANS_PATH, e);
        }

        try {
            CsvManager.initCsvFile(PROMOTERS_PATH);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + PROMOTERS_PATH, e);
        }

    }
    @Override
    public Credential getMusicianCredential(String email) throws DAOException {

        if (getUserCredential(email, MUSICIANS_PATH) != null) {
            return getUserCredential(email, MUSICIANS_PATH);
        }

        throw new DAOException("Couldn't find musician credentials: " + email);

    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        if (getUserCredential(email, PROMOTERS_PATH) != null) {
            return getUserCredential(email, PROMOTERS_PATH);
        }

        throw new DAOException("Couldn't find promoter credentials: " + email);
    }
    
    private Credential getUserCredential(String email, String path){
        
        File file = new File(path);
        
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())){
            
            String line;
            
            while((line = reader.readLine()) != null){
                
                if (line.isBlank()){
                    continue;
                }
                
                Credential credential = parseRowIfMatches(line, email);
                
                if (credential != null){
                    return credential;
                }
                
            }

            return null;
            

        } catch (IOException e) {
            throw new DAOException("Couldn't read credential file: "+path, e);
        }

    }

    private Credential parseRowIfMatches(String line, String email) {
        String[] fields = line.split(CSV_SEPARATOR, -1);
        if (fields.length < NUM_FIELDS || !fields[0].equals(email)) {

            return null;

        }

        return toCredential(fields[0], fields[1]);
    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {
        registerUser(credential, MUSICIANS_PATH);
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {
        registerUser(credential, PROMOTERS_PATH);
    }

    @Override
    public boolean isMusicianAlreadyRegistered(String email) {

        return getUserCredential(email, MUSICIANS_PATH) != null;

    }

    @Override
    public boolean isPromoterAlreadyRegistered(String email) {

        return getUserCredential(email, PROMOTERS_PATH) != null;

    }

    private void registerUser(Credential credential, String path) {
        List<String> lines = readAllLinesAndRegister(credential, path);
        
        File file = new File(path);
        
        try(BufferedWriter writer = Files.newBufferedWriter(file.toPath())){
            
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            
        } catch (IOException e) {
            throw new DAOException("Couldn't write to file " + path, e);
        }
    }

    private List<String> readAllLinesAndRegister(Credential c, String path){

        File file = new File(path);
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(file.toPath())){

            String line;
            
            while((line = reader.readLine()) != null){
                
                if (line.isBlank()){
                    continue;
                }
                
                String[] fields = line.split(CSV_SEPARATOR, -1);
                
                if (fields.length > 0 && fields[0].equals(c.getEmail())){
                    throw new DAOException("Credential for email "+c.getEmail()+" already exists in "+path);
                }
                
                lines.add(line);
            }
            

        }

        catch (IOException e) {
            throw new DAOException("Couldn't read credential file: "+path, e);
        }
        
        lines.add(toCsv(c));
        
        return lines;

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
