package dao.factories;

import dao.announcement.AnnouncementDAO;
import dao.announcement.AnnouncementDAOCsv;
import dao.application.ApplicationDAO;
import dao.application.ApplicationDAOCsv;
import dao.auth.AuthDAO;
import dao.auth.AuthDAOCsv;
import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOCsv;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAOCsv;
import dao.promoter.PromoterDAO;
import dao.promoter.PromoterDAOCsv;

public class DAOFactoryCsv extends DAOFactory {

    @Override
    public AnnouncementDAO getJobAnnouncementDAO() {
        if (this.announcementDAO == null){
            this.announcementDAO = new AnnouncementDAOCsv();
        }

        return this.announcementDAO;
    }

    @Override
    public ApplicationDAO getApplicationDAO() {
        if (this.applicationDAO == null){
            this.applicationDAO = new ApplicationDAOCsv();
        }

        return this.applicationDAO;
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
}
