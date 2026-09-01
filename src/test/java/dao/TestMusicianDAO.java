package dao;

import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import model.Gender;
import model.Mastery;
import model.Instrument;
import model.Musician;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMusicianDAO {

    @Test
    void testAddMusician() {

        Instrument i1 = new Instrument("Electric guitar", Mastery.MASTER);
        Instrument i2 = new Instrument("Piano", Mastery.BEGINNER);
        Instrument i3 = new Instrument("Battery", Mastery.EXPERIENCED);

        List<Instrument> iList = new ArrayList<>();

        iList.add(i1);
        iList.add(i2);
        iList.add(i3);

        Musician m = new Musician("John", "Doe", "BraveJohn", "john@doe.com", Gender.MALE,
                iList);

        MusicianDAO mDAO = DAOFactory.getInstance().getMusicianDAO();

        mDAO.save(m);

        Musician output = mDAO.getMusicianByEmail("john@doe.com");

        assertEquals(m.getArtistName(), output.getArtistName());
        assertEquals(m.getName(), output.getName());
        assertEquals(m.getEmail(), output.getEmail());
        assertEquals(m.getSurname(), output.getSurname());
        assertEquals(m.getGender(), output.getGender());
        assertEquals(m.presentInstruments().getLast().getName(), output.presentInstruments().getLast().getName());
        assertEquals(m.presentInstruments().getLast().getMastery(), output.presentInstruments().getLast().getMastery());

    }

    @Test
    void cacheTest(){
        Instrument i1 = new Instrument("Electric guitar", Mastery.MASTER);
        Instrument i2 = new Instrument("Piano", Mastery.BEGINNER);
        Instrument i3 = new Instrument("Battery", Mastery.EXPERIENCED);

        List<Instrument> iList = new ArrayList<>();

        iList.add(i1);
        iList.add(i2);
        iList.add(i3);

        Musician m = new Musician("John", "Doe", "BraveJohn", "john@doe.com", Gender.MALE,
                iList);

        MusicianDAO mDAO = DAOFactory.getInstance().getMusicianDAO();

        mDAO.save(m);

        boolean output = mDAO.isCached(mDAO.getKey(m));

        assertEquals(true, output);


    }

}
