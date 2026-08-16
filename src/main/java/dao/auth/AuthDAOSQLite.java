package dao.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import engineering.persistency.SQLiteConnection;
import exception.DAOException;
import model.Credential;


public class AuthDAOSQLite extends AuthDAO {

    private static final String MUSICIANS_TABLE = "musician_credentials";
    private static final String PROMOTERS_TABLE = "promoter_credentials";

    private final Connection connection;

    public AuthDAOSQLite() {
        this.connection = SQLiteConnection.getInstance().getConnection();
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {

        String createMusicians =
                "CREATE TABLE IF NOT EXISTS " + MUSICIANS_TABLE + " (" +
                        "email TEXT PRIMARY KEY COLLATE NOCASE," +
                        "crypt_password TEXT NOT NULL)";

        String createPromoters =
                "CREATE TABLE IF NOT EXISTS " + PROMOTERS_TABLE + " (" +
                        "email TEXT PRIMARY KEY COLLATE NOCASE," +
                        "crypt_password TEXT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createMusicians);
            stmt.execute(createPromoters);
        } catch (SQLException e) {
            throw new DAOException("Error creating credential tables", e);
        }
    }

    @Override
    public Credential getMusicianCredential(String email) throws DAOException {
        return getCredential(MUSICIANS_TABLE, email, "musician");
    }

    @Override
    public Credential getPromoterCredential(String email) throws DAOException {
        return getCredential(PROMOTERS_TABLE, email, "promoter");
    }

    @Override
    public void registerMusician(Credential credential) throws DAOException {
        registerCredential(MUSICIANS_TABLE, credential, "musician");
    }

    @Override
    public void registerPromoter(Credential credential) throws DAOException {
        registerCredential(PROMOTERS_TABLE, credential, "promoter");
    }

    private Credential getCredential(String table, String email, String role) throws DAOException {
        String sql = "SELECT email, crypt_password FROM " + table + " WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Credential(rs.getString("email"), rs.getString("crypt_password"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error reading " + role + " credential for email: " + email, e);
        }

        throw new DAOException("No " + role + " credentials found for this email: " + email);
    }

    private void registerCredential(String table, Credential credential, String role) throws DAOException {

        if (invalidEmail(credential.getEmail())) {
            throw new DAOException("Invalid email: " + credential.getEmail());
        }

        String checkSql = "SELECT 1 FROM " + table + " WHERE email = ?";
        String insertSql = "INSERT INTO " + table + " (email, crypt_password) VALUES (?, ?)";

        try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setString(1, credential.getEmail());

            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    throw new DAOException(
                            "A " + role + " credential with this email already exists: " + credential.getEmail());
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error checking existing " + role + " credential", e);
        }

        try (PreparedStatement insertPs = connection.prepareStatement(insertSql)) {
            insertPs.setString(1, credential.getEmail());
            insertPs.setString(2, credential.getCryptPassword());
            insertPs.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error registering " + role + " credential", e);
        }
    }

}
