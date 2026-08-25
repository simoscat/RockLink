package dao.factories;

import dao.announcement.JobAnnouncementDAO;
import dao.announcement.JobAnnouncementDAODemo;
import dao.application.JobApplicationDAO;
import dao.application.JobApplicationDAODemo;
import dao.artist.ArtistDAO;
import dao.artist.ArtistDAODemo;
import dao.auth.AuthDAO;
import dao.auth.AuthDAODemo;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAODemo;
import dao.notification.NotificationDAO;
import dao.notification.NotificationDAODemo;
import dao.promoter.PromoterDAO;
import dao.promoter.PromoterDAODemo;

public class DAOFactoryDemo extends DAOFactory{
    @Override
    public MusicianDAO getMusicianDAO() {
        if (this.musicianDAO == null){
            this.musicianDAO = new MusicianDAODemo();
        }
        return this.musicianDAO;
    }

    @Override
    public PromoterDAO getPromoterDAO() {
        if (this.promoterDAO == null) {
            this.promoterDAO = new PromoterDAODemo();
        }
        return this.promoterDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {
        if (this.authDAO == null) {
            this.authDAO = new AuthDAODemo();
        }
        return this.authDAO;
    }

    @Override
    public JobApplicationDAO getJobApplicationDAO() {
        if (this.jobApplicationDAO == null) {
            this.jobApplicationDAO = new JobApplicationDAODemo();
        }
        return this.jobApplicationDAO;
    }

    @Override
    public JobAnnouncementDAO getJobAnnouncementDAO() {
        if (this.jobAnnouncementDAO == null) {
            this.jobAnnouncementDAO = new JobAnnouncementDAODemo();
        }
        return this.jobAnnouncementDAO;
    }

    @Override
    public ArtistDAO getArtistDAO() {
        if (this.artistDAO == null){
            this.artistDAO = new ArtistDAODemo();
        }
        return this.artistDAO;
    }

    @Override
    public NotificationDAO getNotificationDAO() {
        if (this.notificationDAO == null){
            this.notificationDAO = new NotificationDAODemo();
        }
        return this.notificationDAO;
    }
}
