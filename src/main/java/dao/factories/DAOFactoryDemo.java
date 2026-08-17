package dao.factories;

import dao.announcement.AnnouncementDAO;
import dao.announcement.AnnouncementDAODemo;
import dao.application.ApplicationDAO;
import dao.application.ApplicationDAODemo;
import dao.auth.AuthDAO;
import dao.auth.AuthDAODemo;
import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAODemo;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAODemo;
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
    public InstrumentDAO getInstrumentDAO() {
        if (this.instrumentDAO == null){
            this.instrumentDAO = new InstrumentDAODemo();
        }
        return this.instrumentDAO;
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
