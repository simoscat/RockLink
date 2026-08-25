package dao.factories;

import dao.announcement.JobAnnouncementDAO;
import dao.application.JobApplicationDAO;
import dao.artist.ArtistDAO;
import dao.auth.AuthDAO;
import dao.musician.MusicianDAO;
import dao.notification.NotificationDAO;
import dao.promoter.PromoterDAO;
import engineering.persistency.ConfigManager;




public abstract class DAOFactory {
    private static DAOFactory instance = null;

    protected MusicianDAO musicianDAO = null;
    protected PromoterDAO promoterDAO = null;
    protected AuthDAO authDAO = null;
    protected JobApplicationDAO jobApplicationDAO = null;
    protected JobAnnouncementDAO jobAnnouncementDAO = null;
    protected ArtistDAO artistDAO = null;
    protected NotificationDAO notificationDAO = null;

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            String persistenceType = ConfigManager.getProperty("persistence.type", "JSON").toUpperCase();
            instance = switch (persistenceType) {
                case "JSON" -> new DAOFactoryJson();
                case "CSV" -> new DAOFactoryCsv();
                case "DEMO" -> new DAOFactoryDemo();
                default -> new DAOFactoryJson(); // Default fallback
            };
        }
        return instance;
    }

    public abstract JobAnnouncementDAO getJobAnnouncementDAO();
    public abstract JobApplicationDAO getJobApplicationDAO();
    public abstract AuthDAO getAuthDAO();
    public abstract MusicianDAO getMusicianDAO();
    public abstract PromoterDAO getPromoterDAO();
    public abstract ArtistDAO getArtistDAO();
    public abstract NotificationDAO getNotificationDAO();
}
