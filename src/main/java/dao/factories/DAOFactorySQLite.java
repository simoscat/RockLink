package dao.factories;

import dao.auth.AuthDAO;
import dao.auth.AuthDAOSQLite;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;

public class DAOFactorySQLite extends DAOFactory{
    @Override
    public MusicianDAO getMusicianDAO() {
        if (this.musicianDAO == null){
            this.musicianDAO = new MusicianDAOSQLite();
        }
        return this.musicianDAO;
    }
    
    @Override
    public PromoterDAO getPromoterDAO() {
        if (this.promoterDAO == null) {
            this.promoterDAO = new PromoterDAOSQLite();
        }
        return this.promoterDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {
        if (this.authDAO == null) {
            this.authDAO = new AuthDAOSQLite();
        }
        return this.authDAO;
    }

    @Override
    public ApplicationDAO getApplicationDAO() {
        if (this.applicationDAO == null) {
            this.applicationDAO = new ApplicationDAOSQLite();
        }
        return this.applicationDAO;
    }

    @Override
    public AnnouncementDAO getAnnouncementDAO() {
        if (this.announcementDAO == null) {
            this.announcementDAO = new AnnouncementDAOSQLite();
        }
        return this.announcementDAO;
    }
}
