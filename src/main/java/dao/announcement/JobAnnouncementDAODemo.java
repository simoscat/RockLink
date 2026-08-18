package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import exception.DAOException;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.jobAnnouncementDecorators.ExpertsOnlyDecoratorJob;
import model.jobAnnouncementDecorators.UrgentJobAnnouncementDecorator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class JobAnnouncementDAODemo extends JobAnnouncementDAO{

    private List<JobAnnouncement> jobAnnouncements;

    public JobAnnouncementDAODemo(){

        jobAnnouncements = new ArrayList<>();

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();

        JobAnnouncement job1 = new ConcreteJobAnnouncement(
                "Guitarist for Rock Night at Largo Venue",
                "We are looking for a skilled guitarist to perform at our Rock Night event." +
                        "Improvisation skills are a must. Contact us for more details.",
                LocalDateTime.of(2026, 10, 15, 21, 0),
                JobAnnouncementStatus.OPEN,
                LocalDateTime.now(ZoneId.systemDefault()),
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(150), CurrencyType.EUR),
                "Via Biordo Michelotti, 2, 00176 Roma RM"
        );

        JobAnnouncement job2 = new ConcreteJobAnnouncement(
                "Sound Engineer for Jazz Night at Blue Note",
                "We are urgently looking for an expert sound engineer to manage a jazz night at Blue Note." +
                        "Contact us for more information.",
                LocalDateTime.of(2026, 9, 9, 19, 30),
                JobAnnouncementStatus.OPEN,
                LocalDateTime.now(ZoneId.systemDefault()),
                promoterDAO.getPromoterByEmail("marco.santodonato@libero.it"),
                new MoneyValue(new BigDecimal(350), CurrencyType.USD),
                "Via Pietro Borsieri, 37, Milan, IT 20159"
        );

        JobAnnouncement job2Dec = new UrgentJobAnnouncementDecorator(new ExpertsOnlyDecoratorJob(job2));

        jobAnnouncements.add(job1);
        jobAnnouncements.add(job2Dec);

    }

    @Override
    public JobAnnouncement retrieveJobAnnouncementById(String id) {

        for (JobAnnouncement job : jobAnnouncements) {

            String uid = getUniqueId(job);

            if (uid.equals(id)){
                return job;
            }

        }

        throw new DAOException("Couldn't find job with id: "+id);

    }

    @Override
    public String getUniqueId(JobAnnouncement job) {
//        return job.getPublisher().getEmail() + "~" +
//                job.getTitle().replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9_]", "") + "~" +
//                job.getAnnouncementPublishDate().toString();

        return job.getPublisher().getEmail() + "~" + job.getAnnouncementPublishDate().toString();

    }

    @Override
    protected List<JobAnnouncement> retrieveAllJobAnnouncements() {

        return jobAnnouncements;

    }

    @Override
    protected void saveToPersistency(JobAnnouncement obj) {

        jobAnnouncements.add(obj);

    }

    private String uniformTitle(String title){
        return title.replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9]", "");
    }
}
