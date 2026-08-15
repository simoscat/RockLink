package dao.factories;

import dao.auth.AuthDAO;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public abstract class DAOFactory {
    private static DAOFactory instance = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            try (InputStream in = new FileInputStream("config.properties")) {
                Properties properties = new Properties();
                properties.load(in);
                String persistenceType = properties.getProperty("persistence.type", "JSON").toUpperCase();
                instance = switch (persistenceType) {
                    case "JSON" -> new DAOFactoryJSON();
                    case "OTHER" -> new DAOFactoryOther();
                    case "DEMO" -> new DaoFactoryDemo();
                    default -> new DaoFactoryJSON(); // Default fallback
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
