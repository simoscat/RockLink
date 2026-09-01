package decorator;

import model.CurrencyType;
import model.Gender;
import model.JobAnnouncementStatus;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import model.jobannouncementdecorators.NegotiableSalaryDecorator;
import model.jobannouncementdecorators.UrgentDecorator;
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
                new Promoter("Mario", "Rossi", "mariorossi@gmail.com", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(10), CurrencyType.EUR),
                "Via Mario Rossi 19, Roma RM"
        );

        j.publishNow();

        JobAnnouncement uD = new UrgentDecorator(j);

        String output = uD.getTitle();
        String expected = "[Urgent] "+ title;

        assertEquals(expected, output);

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
                new Promoter("Mario", "Rossi", "mariorossi@gmail.com", Gender.MALE, contacts),
                new MoneyValue(new BigDecimal(10), CurrencyType.EUR),
                "Via Mario Rossi 19, Roma RM"
        );

        j.publishNow();

        JobAnnouncement uD = new UrgentDecorator(j);
        JobAnnouncement nS = new NegotiableSalaryDecorator(uD);

        String output = nS.getTitle();
        String expected = "[Negotiable Salary] [Urgent] "+ title;

        assertEquals(expected, output);

    }
}
