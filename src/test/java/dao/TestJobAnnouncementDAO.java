package dao;

import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import engineering.enums.CurrencyType;
import engineering.enums.Gender;
import engineering.enums.JobAnnouncementStatus;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import model.jobannouncementdecorators.NegotiableSalaryDecoratorJob;
import model.jobannouncementdecorators.UrgentJobAnnouncementDecorator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestJobAnnouncementDAO {

    @Test
    void testAddDecoratedJobAnnouncement(){

        Map<String, String> contacts = new HashMap<>();
        contacts.put("phone", "123 456 8123");

        JobAnnouncement jobAnnouncement = new ConcreteJobAnnouncement(
                "Looking for guitarist for live performance in Capannelle",
                "Hello",
                LocalDateTime.of(2026, 12, 10, 20, 45),
                JobAnnouncementStatus.OPEN,
                LocalDateTime.now(),
                new Promoter("Pro", "Moter", "promoter@libero.it", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(100), CurrencyType.EUR),
                "Via Mario Rossi 12, Roma RM"
        );

        jobAnnouncement = new UrgentJobAnnouncementDecorator(jobAnnouncement);
        jobAnnouncement = new NegotiableSalaryDecoratorJob(jobAnnouncement);

        JobAnnouncementDAO jobAnnDao = DAOFactory.getInstance().getJobAnnouncementDAO();

        jobAnnDao.save(jobAnnouncement);

        String id = jobAnnDao.getUniqueId(jobAnnouncement);

        JobAnnouncement output = jobAnnDao.getAnnouncementFromId(id);

        assertEquals(jobAnnouncement.getTitle(), output.getTitle());
        assertEquals(jobAnnouncement.getContent(), output.getContent());
        assertEquals(jobAnnouncement.getAnnouncementDate(), output.getAnnouncementDate());
        assertEquals(jobAnnouncement.getStatus(), output.getStatus());
        assertEquals(jobAnnouncement.getAnnouncementPublishDate(), output.getAnnouncementPublishDate());
        assertEquals(jobAnnouncement.getEventAddress(), output.getEventAddress());
        assertEquals(jobAnnouncement.getJobPay(), output.getJobPay());
    }

}
