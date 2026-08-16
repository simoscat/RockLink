package dao.musician;

import engineering.persistency.DAOWithCache;
import model.Musician;

public abstract class MusicianDAO extends DAOWithCache<Musician> {

    @Override
    public String getKey(Musician obj) {
        return obj.getEmail();
    }

    public abstract Musician getMusicianByEmail(String email);

    public abstract void flush(Musician m);

}
