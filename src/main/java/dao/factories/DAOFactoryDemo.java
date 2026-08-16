package dao.factories;

import dao.auth.AuthDAO;
import dao.auth.AuthDAODemo;
import dao.auth.AuthDAOSQLite;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAODemo;
import dao.promoter.PromoterDAO;

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
    public ApplicationDAO getApplicationDAO() {
        if (this.applicationDAO == null) {
            this.applicationDAO = new ApplicationDAODemo();
        }
        return this.applicationDAO;
    }

    @Override
    public AnnouncementDAO getAnnouncementDAO() {
        if (this.announcementDAO == null) {
            this.announcementDAO = new AnnouncementDAODemo();
        }
        return this.announcementDAO;
    }
}
