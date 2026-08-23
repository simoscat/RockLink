package dao.artist;

import model.Artist;

public abstract class ArtistDAO {

    public abstract Artist getArtistByEmail(String email);
    //the implementation is the same everywhere because bands aren't implemented yet

    public abstract void save(Artist a);

}
