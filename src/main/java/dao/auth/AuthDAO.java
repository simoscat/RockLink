package dao.auth;

import dao.factories.DAOFactory;
import engineering.EmailChecker;
import exception.DAOException;
import model.Credential;

public abstract class AuthDAO {


    //common logic
    protected boolean invalidEmail(String email) {
        return !EmailChecker.isValidEmail(email);
    }

    public abstract Credential getMusicianCredential(String email) throws DAOException;

    public abstract Credential getPromoterCredential(String email) throws DAOException;

    public abstract void registerMusician(Credential credential) throws DAOException;
    
    public abstract void registerPromoter(Credential credential) throws DAOException;

    public abstract boolean isMusicianAlreadyRegistered(String email);

    public abstract boolean isPromoterAlreadyRegistered(String email);

}
