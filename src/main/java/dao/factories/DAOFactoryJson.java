package dao.factories;

import dao.auth.AuthDAO;
import dao.auth.AuthDAOJson;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;

public class DAOFactoryJson extends DAOFactory {
    @Override
    public MusicianDAO getMusicianDAO() {
        if (this.musicianDAO == null) {
            this.musicianDAO = new MusicianDAOJson();
        }

        return this.musicianDAO;
    }

    @Override
    public PromoterDAO getPromoterDAO() {
        if (this.promoterDAO == null) {
            this.promoterDAO = new PromoterDAOJson();
        }

        return this.promoterDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {
        if (this.authDAO == null) {
            this.authDAO = new AuthDAOJson();
        }

        return this.authDAO;
    }

    @Override
    public ApplicationDAO getApplicationDAO() {
        if (this.applicationDAO == null) {
            this.applicationDAO = new ApplicationDAOJson();
        }

        return this.applicationDAO;
    }

    @Override
    public AnnouncementDAO getAnnouncementDAO() {
        if (this.announcementDAO == null) {
            this.announcementDAO = new AnnouncementDAOJson();
        }

        return this.announcementDAO;
    }
}
