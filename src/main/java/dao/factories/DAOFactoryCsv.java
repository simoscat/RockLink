package dao.factories;

import dao.artist.ArtistDAO;
import dao.artist.ArtistDAOCsv;
import dao.auth.AuthDAO;
import dao.auth.AuthDAOCsv;
import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOCsv;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAOCsv;
import dao.notification.NotificationDAO;
import dao.notification.NotificationDAOCsv;
import dao.promoter.PromoterDAO;
import dao.promoter.PromoterDAOCsv;
import dao.announcement.JobAnnouncementDAO;
import dao.announcement.JobAnnouncementDAOCsv;
import dao.application.JobApplicationDAO;
import dao.application.JobApplicationDAOCsv;

public class DAOFactoryCsv extends DAOFactory {

    @Override
    public JobAnnouncementDAO getJobAnnouncementDAO() {
        if (this.jobAnnouncementDAO == null){
            this.jobAnnouncementDAO = new JobAnnouncementDAOCsv();
        }

        return this.jobAnnouncementDAO;
    }

    @Override
    public JobApplicationDAO getJobApplicationDAO() {
        if (this.jobApplicationDAO == null){
            this.jobApplicationDAO = new JobApplicationDAOCsv();
        }

        return this.jobApplicationDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {
        if (this.authDAO == null){
            this.authDAO = new AuthDAOCsv();
        }

        return this.authDAO;
    }

    @Override
    public InstrumentDAO getInstrumentDAO() {
        if (this.instrumentDAO == null){
            this.instrumentDAO = new InstrumentDAOCsv();
        }
        return this.instrumentDAO;
    }

    @Override
    public MusicianDAO getMusicianDAO() {
        if (this.musicianDAO == null){
            this.musicianDAO = new MusicianDAOCsv();
        }

        return this.musicianDAO;
    }

    @Override
    public PromoterDAO getPromoterDAO() {
        if (this.promoterDAO == null){
            this.promoterDAO = new PromoterDAOCsv();
        }

        return this.promoterDAO;
    }

    @Override
    public ArtistDAO getArtistDAO() {
        if (this.artistDAO == null){
            this.artistDAO = new ArtistDAOCsv();
        }
        return this.artistDAO;
    }

    @Override
    public NotificationDAO getNotificationDAO() {
        if (this.notificationDAO == null){
            this.notificationDAO = new NotificationDAOCsv();
        }
        return this.notificationDAO;
    }
}
