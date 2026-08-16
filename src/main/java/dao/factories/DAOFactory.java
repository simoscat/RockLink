package dao.factories;

import dao.auth.AuthDAO;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;




public abstract class DAOFactory {
    private static DAOFactory instance = null;

    protected MusicianDAO musicianDAO = null;
    protected PromoterDAO promoterDAO = null;
    protected AuthDAO authDAO = null;
    protected ApplicationDAO applicationDAO = null;
    protected AnnouncementDAO announcementDAO = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            try (InputStream in = new FileInputStream("config.properties")) {
                Properties properties = new Properties();
                properties.load(in);
                String persistenceType = properties.getProperty("persistence.type", "JSON").toUpperCase();
                instance = switch (persistenceType) {
                    case "JSON" -> new DAOFactoryJSON();
                    case "SQLITE" -> new DAOFactorySQLite();
                    case "DEMO" -> new DAOFactoryDemo();
                    default -> new DAOFactoryJSON(); // Default fallback
                };
            } catch (IOException e) {
                throw new DAOException("Error reading config.properties", e);
            }
        }
        return instance;
    }

    public abstract MusicianDAO getMusicianDAO();
    public abstract PromoterDAO getPromoterDAO();
    public abstract AuthDAO getAuthDAO();
    public abstract ApplicationDAO getApplicationDAO();
    public abstract AnnouncementDAO getAnnouncementDAO();
}
