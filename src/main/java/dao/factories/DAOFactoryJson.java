package dao.factories;

import dao.artist.ArtistDAO;
import dao.artist.ArtistDAOJson;
import dao.auth.AuthDAO;
import dao.auth.AuthDAOJson;
import dao.instrument.InstrumentDAO;
import dao.instrument.InstrumentDAOJson;
import dao.musician.MusicianDAO;
import dao.musician.MusicianDAOJson;
import dao.promoter.PromoterDAO;
import dao.announcement.JobAnnouncementDAO;
import dao.announcement.JobAnnouncementDAOJson;
import dao.application.JobApplicationDAO;
import dao.application.JobApplicationDAOJson;
import dao.promoter.PromoterDAOJson;

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
    public InstrumentDAO getInstrumentDAO() {
        if (this.instrumentDAO == null) {
            this.instrumentDAO = new InstrumentDAOJson();
        }
        return this.instrumentDAO;
    }

    @Override
    public JobApplicationDAO getJobApplicationDAO() {
        if (this.jobApplicationDAO == null) {
            this.jobApplicationDAO = new JobApplicationDAOJson();
        }

        return this.jobApplicationDAO;
    }

    @Override
    public JobAnnouncementDAO getJobAnnouncementDAO() {
        if (this.jobAnnouncementDAO == null) {
            this.jobAnnouncementDAO = new JobAnnouncementDAOJson();
        }

        return this.jobAnnouncementDAO;
    }

    @Override
    public ArtistDAO getArtistDAO() {
        if (this.artistDAO == null){
            this.artistDAO = new ArtistDAOJson();
        }
        return this.artistDAO;
    }
}
