package dao.auth;

import engineering.ConfigManager;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Credential;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
