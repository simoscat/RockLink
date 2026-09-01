package dao;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import model.Gender;
import model.Promoter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPromoterDAO {

    @Test
    void testAddPromoter(){

        Map<String, String> contacts = new HashMap<>();

        contacts.put("phone", "+39 123 451 1921");

        Promoter p = new Promoter("Maria", "Bianchi", "mariabianchi@gmail.com", Gender.FEMALE,
                contacts);

        PromoterDAO pDAO = DAOFactory.getInstance().getPromoterDAO();

        pDAO.save(p);

        Promoter output = pDAO.getPromoterByEmail("mariabianchi@gmail.com");

        assertEquals(p.getName(), output.getName());
        assertEquals(p.getEmail(), output.getEmail());
        assertEquals(p.getSurname(), output.getSurname());
        assertEquals(p.getGender(), output.getGender());
        assertEquals(p.howToContact().get("phone"), output.howToContact().get("phone"));

    }

}
