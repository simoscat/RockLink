package dao.auth;

import exception.DAOException;
import model.Credential;

public abstract class AuthDAO {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]++(?:\\.[a-zA-Z0-9_+&*-]++)*+@"
                    + "(?:[a-zA-Z0-9-]++\\.)++[a-zA-Z]{2,7}$";;

    protected boolean invalidEmail(String email) {
        return !email.matches(EMAIL_REGEX);
    }

    public abstract Credential getMusicianCredential(String email) throws DAOException;

    public abstract Credential getPromoterCredential(String email) throws DAOException;

    public abstract void registerMusician(Credential credential) throws DAOException;
    
    public abstract void registerPromoter(Credential credential) throws DAOException;
}
