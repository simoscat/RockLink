package dao.auth;

import exception.DAOException;
import model.Credential;
import model.UserType;

public interface AuthDAO {

    public abstract Credential getUserCredential(String email, UserType type) throws DAOException;

    public abstract void registerUser(Credential credential, UserType type) throws DAOException;

}
