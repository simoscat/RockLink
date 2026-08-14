package dao.auth;

import exception.DAOException;
import model.Credential;
import model.UserType;

public class JSONAuthDAO implements AuthDAO {

    private final String BASEPATH = "data/";

    @Override
    public Credential getUserCredential(String email, UserType type) throws DAOException {

        String filename = this.BASEPATH + type.name().toLowerCase() + ".json";



    }

    @Override
    public void registerUser(Credential credential, UserType type) throws DAOException {

    }
}
