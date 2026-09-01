package dao.promoter;

import engineering.persistency.ConfigManager;
import model.Gender;
import exception.DAOException;
import model.Promoter;

import java.util.HashMap;
import java.util.Map;

public class PromoterDAODemo extends PromoterDAO {

    private final String email;

    public PromoterDAODemo(){
        this.email = ConfigManager.getProperty("demo.promoter");
    }

    @Override
    protected Promoter retrievePromoterByEmail(String email) {

        Map<String, String> contacts = new HashMap<>();

        contacts.put("Email di lavoro", "marco.santodonato@promotions.com");
        contacts.put("Telefono", "+39 123 456 7890");

        if (this.email.equals(email)) {
            return new Promoter("Marco", "Santodonato", "marco.santodonato@libero.it", Gender.MALE, contacts);
        }

        // promoters from DAO are returned without their job announcements,
        // as they are retrieved separately from the AnnouncementDAO

        throw new DAOException("No promoter found with email: " + email);
    }

    @Override
    protected void saveToPersistency(Promoter p) {
        //nothing to do
    }
}
