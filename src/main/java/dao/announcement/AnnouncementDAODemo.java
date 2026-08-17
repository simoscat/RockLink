package dao.announcement;

import dao.factories.DAOFactory;
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
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAODemo extends AnnouncementDAO {

    List<Announcement> jobs;

    public AnnouncementDAODemo(){

        jobs = new ArrayList<>();

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();

        Announcement job1 = new JobAnnouncement(
                "0",
                "Guitarist for Rock Night at Largo Venue",
                "We are looking for a skilled guitarist to perform at our Rock Night event." +
                        "Improvisation skills are a must. Contact us for more details.",
                LocalDateTime.of(2026, 9, 15, 21, 0),
                AnnouncementStatus.OPEN,
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(150), CurrencyEnum.EUR),
                "Via Michelotti Biordo 2, Rome, IT 00176"
        );

        Announcement job2 = new JobAnnouncement(
                "1",
                "Sound Engineer for Jazz Night at Blue Note",
                "We are urgently looking for an expert sound engineer to manage a jazz night at Blue Note." +
                        "Contact us for more information.",
                LocalDateTime.of(2026, 9, 9, 19, 30),
                AnnouncementStatus.OPEN,
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(200), CurrencyEnum.EUR),
                "Via Pietro Borsieri, 37, Milan, IT 20159"
        );

        Announcement job2U = new UrgentAnnouncementDecorator(job2);
        Announcement job2E = new ExpertsOnlyDecorator(job2U);

        jobs.add(job1);
        jobs.add(job2E);
    }


    @Override
    protected Announcement retrieveAnnouncementById(String id) {

        for (Announcement job : jobs) {
            if (job.getId().equals(id)) {
                return job;
            }
        }

        throw new DAOException("No job announcement found with id: " + id);
    }

    @Override
    protected void saveToPersistency(Announcement announcement) {
        //nothing to do in demo
    }
}
