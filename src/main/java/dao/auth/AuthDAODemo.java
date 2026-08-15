package dao.auth;


import exception.DAOException;
import model.Credential;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthDAODemo extends AuthDAO{

    private String musicianMail;
    private String promoterMail;
    private String password;

    public AuthDAODemo() {
        Properties props  = new Properties();

        try(FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new DAOException("Error reading config.properties", e);
        }

        musicianMail = props.getProperty("demo.musician");
        promoterMail = props.getProperty("demo.promoter");
        password = props.getProperty("demo.password");

    }

    @Override
    public Credential getMusicianCredential(String email) throws DAOException {
        if (musicianMail.equals(email)) {
            return new Credential(musicianMail, password);
        }
        else throw new DAOException("No musician credentials found for this email: " + email);
    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        if (promoterMail.equals(email)) {
            return new Credential(promoterMail, password);
        }
        else throw new DAOException("No promoter credentials found for this email: " + email);
    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {
        throw new DAOException("Sign-in is disabled in Demo mode"); //nothing to do
    }
    
    @Override
    public void registerPromoter(Credential credential) throws DAOException {
        throw new DAOException("Sign-in is disabled in Demo mode"); //nothing to do
    }
}
