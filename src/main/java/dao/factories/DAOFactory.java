package dao.factories;

import dao.announcement.AnnouncementDAO;
import dao.application.ApplicationDAO;
import dao.auth.AuthDAO;
import dao.instrument.InstrumentDAO;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;




public abstract class DAOFactory {
    private static DAOFactory instance = null;

    protected MusicianDAO musicianDAO = null;
    protected PromoterDAO promoterDAO = null;
    protected AuthDAO authDAO = null;
    protected ApplicationDAO applicationDAO = null;
    protected AnnouncementDAO announcementDAO = null;
    protected InstrumentDAO instrumentDAO = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            try (InputStream in = new FileInputStream("config.properties")) {
                Properties properties = new Properties();
                properties.load(in);
                String persistenceType = properties.getProperty("persistence.type", "JSON").toUpperCase();
                instance = switch (persistenceType) {
                    case "JSON" -> new DAOFactoryJson();
                    case "CSV" -> new DAOFactoryCsv();
                    case "DEMO" -> new DAOFactoryDemo();
                    default -> new DAOFactoryJson(); // Default fallback
                };
            } catch (IOException e) {
                throw new DAOException("Error reading config.properties", e);
            }
        }
        return instance;
    }

    public abstract AnnouncementDAO getJobAnnouncementDAO();
    public abstract ApplicationDAO getApplicationDAO();
    public abstract AuthDAO getAuthDAO();
    public abstract InstrumentDAO getInstrumentDAO();
    public abstract MusicianDAO getMusicianDAO();
    public abstract PromoterDAO getPromoterDAO();


}
