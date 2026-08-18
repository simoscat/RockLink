package dao.promoter;

import engineering.enums.Gender;
import exception.DAOException;
import model.BaseAnnouncement;
import model.Musician;
import model.Promoter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PromoterDAODemo extends PromoterDAO {

    private String email;

    public PromoterDAODemo(){
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();

            prop.load(is);
            this.email = prop.getProperty("demo.promoter");
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file: " + e.getMessage());
        }
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
