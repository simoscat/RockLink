package dao.musician;

import dao.DAOWithCache;
import model.Musician;

public abstract class MusicianDAO extends DAOWithCache<Musician> {

    @Override
    public String getKey(Musician obj) {
        return obj.getEmail();
    }

    public abstract Musician getMusicianByEmail(String email);
    public abstract Musician flush(Musician m);
}
