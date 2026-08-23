package dao;

import dao.announcement.JobAnnouncementDAO;
import dao.application.JobApplicationDAO;
import dao.factories.DAOFactory;
import engineering.enums.CurrencyType;
import engineering.enums.Gender;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.Mastery;
import model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestJobApplicationDAO {

    @Test
    void testJobApplicationAndJobAnnouncementDAO(){

        JobApplicationDAO jobApplicationDAO = DAOFactory.getInstance().getJobApplicationDAO();

        Map<String, String> contacts = new HashMap<>();
        contacts.put("phone", "123 456 8123");

        JobAnnouncement jobAnnouncement = new ConcreteJobAnnouncement(
                "Looking for guitarist for live performance in Capannelle",
                "Hello",
                LocalDateTime.of(2026, 12, 10, 20, 45),
                JobAnnouncementStatus.OPEN,
                new Promoter("Pro", "Moter", "promoter@libero.it", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(100), CurrencyType.EUR),
                "Via Mario Rossi 12, Roma RM"
        );

        jobAnnouncement.publishNow();

        Instrument i1 = new Instrument("Electric guitar", Mastery.MASTER);
        Instrument i2 = new Instrument("Piano", Mastery.BEGINNER);
        Instrument i3 = new Instrument("Battery", Mastery.EXPERIENCED);

        List<Instrument> iList = new ArrayList<>();

        iList.add(i1);
        iList.add(i2);
        iList.add(i3);

        Musician m = new Musician("John", "Doe", "BraveJohn", "john@doe.com", Gender.MALE,
                iList);

        JobApplication jobApp = new JobApplication(
                jobAnnouncement,
                m);

        jobApplicationDAO.save(jobApp);

        JobApplication output = jobApplicationDAO.getJobApplication("john@doe.com", jobAnnouncement);

        assertEquals(jobApp.currentApplicationStatus(), output.currentApplicationStatus());
        assertEquals(jobApp.whichJobAnnouncement(), output.whichJobAnnouncement());


    }

}
