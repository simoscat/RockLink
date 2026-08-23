package dao.auth;


import engineering.ConfigManager;
import exception.DAOException;
import model.Credential;

public class AuthDAODemo extends AuthDAO{

    private final String musicianMail;
    private final String promoterMail;
    private final String password;

    public AuthDAODemo() {
        musicianMail = ConfigManager.getProperty("demo.musician");
        promoterMail = ConfigManager.getProperty("demo.promoter");
        password = ConfigManager.getProperty("demo.password");
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

    @Override
    public boolean isMusicianAlreadyRegistered(String email) {
        return musicianMail.equals(email);
    }

    @Override
    public boolean isPromoterAlreadyRegistered(String email) {
        return promoterMail.equals(email);
    }
}
