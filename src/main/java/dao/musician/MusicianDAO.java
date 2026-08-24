package dao.musician;

import dao.DAOWithCache;
import model.Musician;

import java.util.List;

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

    public List<String> getAllMusicianEmails(){

        return retrieveAllEmails();

    }

    public abstract Musician retrieveMusicianByEmail(String email);
    public abstract List<String> retrieveAllEmails();
}
