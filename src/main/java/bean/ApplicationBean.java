package bean;

import model.Artist;

public abstract class ApplicationBean {
    
    private Artist artist;
    private String status;

    protected ApplicationBean() {
    }

    protected ApplicationBean(Artist artist, String status) {
        this.artist = artist;
        this.status = status;
    }


    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
