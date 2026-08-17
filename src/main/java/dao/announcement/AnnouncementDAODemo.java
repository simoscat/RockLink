package dao.announcement;

import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import engineering.enums.AnnouncementStatus;
import engineering.enums.CurrencyEnum;
import exception.DAOException;
import model.Announcement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.decorators.ExpertsOnlyDecorator;
import model.decorators.UrgentAnnouncementDecorator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAODemo extends AnnouncementDAO {

    List<Announcement> announcements;

    public AnnouncementDAODemo(){

        announcements = new ArrayList<>();

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
        MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();

        Announcement job1 = new JobAnnouncement(
                "Guitarist for Rock Night at Largo Venue",
                "We are looking for a skilled guitarist to perform at our Rock Night event." +
                        "Improvisation skills are a must. Contact us for more details.",
                LocalDateTime.of(2026, 9, 15, 21, 0),
                AnnouncementStatus.OPEN,
                LocalDateTime.now(ZoneId.systemDefault()),
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(150), CurrencyEnum.EUR),
                "Via Biordo Michelotti, 2, 00176 Roma RM"
        );

        Announcement job2 = new JobAnnouncement(
                "Sound Engineer for Jazz Night at Blue Note",
                "We are urgently looking for an expert sound engineer to manage a jazz night at Blue Note." +
                        "Contact us for more information.",
                LocalDateTime.of(2026, 9, 9, 19, 30),
                AnnouncementStatus.FILLED,
                LocalDateTime.now(),
                musicianDAO.getMusicianByEmail("anna.muscatello@gmail.com"),
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(200), CurrencyEnum.EUR),
                "Via Pietro Borsieri, 37, Milan, IT 20159"
        );

        Announcement job2U = new UrgentAnnouncementDecorator(job2);
        Announcement job2E = new ExpertsOnlyDecorator(job2U);

        announcements.add(job1);
        announcements.add(job2E);
    }


    @Override
    protected Announcement retrieveAnnouncementById(String id) {

        try{
            return this.announcements.get(Integer.parseInt(id));
        }
        catch(IndexOutOfBoundsException e){
            throw new DAOException("Announcement ID not found: "+id, e);
        }

    }

    @Override
    protected void saveToPersistency(Announcement announcement) {
        announcement.publishNow();
        announcements.add(announcement);
    }
}
