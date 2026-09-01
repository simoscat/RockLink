package dao.instrument;

import model.Mastery;
import exception.DAOException;
import model.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentDAODemo extends InstrumentDAO{
    @Override
    public List<Instrument> getMusicianInstruments(String musicianEmail) {

        if (musicianEmail.equals("anna.muscatello@gmail.com")){

            List<Instrument> instrumentList = new ArrayList();

            Instrument c = new Instrument("Chitarra elettrica", Mastery.MASTER);

            Instrument c1 = new Instrument("Chitarra acustica", Mastery.EXPERIENCED);

            Instrument b = new Instrument("Basso", Mastery.INTERMEDIATE);

            Instrument b1 = new Instrument("Batteria", Mastery.BEGINNER);

            instrumentList.add(c);
            instrumentList.add(c1);
            instrumentList.add(b);
            instrumentList.add(b1);

            return instrumentList;
        }

        throw new DAOException("No instruments found for this musician: "+musicianEmail);

    }

    @Override
    public void saveMusicianInstruments(String musicianEmail, List<Instrument> instruments) {
        //nothing to do in demo
    }

}
