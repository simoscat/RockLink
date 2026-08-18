package decorator;

import engineering.enums.CurrencyType;
import engineering.enums.Gender;
import engineering.enums.JobAnnouncementStatus;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import model.jobAnnouncementDecorators.NegotiableSalaryDecoratorJob;
import model.jobAnnouncementDecorators.UrgentJobAnnouncementDecorator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDecorator {

    @Test
    void testUrgentDecorator(){

        Map<String, String> contacts = new HashMap<>();

        contacts.put("phone", "132 141 1821");

        String title = "Hello";

        JobAnnouncement j = new ConcreteJobAnnouncement(
                title,
                "",
                LocalDateTime.now(),
                JobAnnouncementStatus.OPEN,
                LocalDateTime.now(),
                new Promoter("Mario", "Rossi", "mariorossi@gmail.com", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(10), CurrencyType.EUR),
                "Via Mario Rossi 19, Roma RM"
        );

        JobAnnouncement uD = new UrgentJobAnnouncementDecorator(j);

        String output = uD.getTitle();
        String expected = "[Urgent] "+ title;

        assertEquals(output, expected);

    }

    @Test
    void testDecoratorComposition(){

        Map<String, String> contacts = new HashMap<>();

        contacts.put("phone", "132 141 1821");

        String title = "Hello";

        JobAnnouncement j = new ConcreteJobAnnouncement(
                title,
                "",
                LocalDateTime.now(),
                JobAnnouncementStatus.OPEN,
                LocalDateTime.now(),
                new Promoter("Mario", "Rossi", "mariorossi@gmail.com", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(10), CurrencyType.EUR),
                "Via Mario Rossi 19, Roma RM"
        );

        JobAnnouncement uD = new UrgentJobAnnouncementDecorator(j);
        JobAnnouncement nS = new NegotiableSalaryDecoratorJob(uD);

        String output = nS.getTitle();
        String expected = "[Negotiable Salary] [Urgent] "+ title;

        assertEquals(output, expected);

    }
}
