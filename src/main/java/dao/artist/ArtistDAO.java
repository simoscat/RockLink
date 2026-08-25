package dao.artist;

import dao.factories.DAOFactory;
import engineering.enums.ArtistType;
import model.Artist;
import model.Musician;

//this DAO works as a facade for musicianDAO and the future bandDAO, and it is meant
//to be used by announcements and applications

public class ArtistDAO {

    public Artist getArtistByEmail(String email) {
        return DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(email);
    }

    public void save(Artist a) {

        if (a.getType().equals(ArtistType.MUSICIAN)){
            DAOFactory.getInstance().getMusicianDAO().save((Musician) a);
        }

        //here will be the code for bands

    }

}
