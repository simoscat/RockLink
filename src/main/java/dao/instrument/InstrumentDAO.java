package dao.instrument;

import engineering.persistency.DAOWithCache;
import model.Instrument;

import java.util.List;

public abstract class InstrumentDAO {

    public abstract List<Instrument> getMusicianInstruments(String musicianEmail);

    public abstract void saveMusicianInstruments(String musicianEmail, List<Instrument> instruments);

}
