package dao.artist;

import dao.factories.DAOFactory;
import engineering.enums.ArtistType;
import model.Artist;
import model.Musician;

public class ArtistDAOCsv extends ArtistDAO {
    @Override
    public Artist getArtistByEmail(String email) {
        return DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(email);
    }

    @Override
    public void save(Artist a) {

        if (a.getType().equals(ArtistType.MUSICIAN)){
            DAOFactory.getInstance().getMusicianDAO().save((Musician) a);
        }

        //here will be the code for bands

    }
}
