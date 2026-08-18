package dao.musician;

import engineering.persistency.DAOWithCache;
import model.Musician;

public abstract class MusicianDAO extends DAOWithCache<Musician> {

    @Override
    public String getKey(Musician obj) {
        return obj.getEmail();
    }

    public Musician getMusicianByEmail(String email){
        if (this.isCached(email)){
            return this.getFromCache(email);
        } else {
            Musician m = this.retrieveMusicianByEmail(email);
            this.addToCache(m);
            return m;
        }
    }

    public abstract Musician retrieveMusicianByEmail(String email);
}
