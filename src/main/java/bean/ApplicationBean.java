package bean;

public abstract class ApplicationBean {
    
    private ArtistBean artist;
    private String status;


    protected ApplicationBean(ArtistBean artist, String status) {
        this.artist = artist;
        this.status = status;
    }


    public ArtistBean getArtist() {
        return artist;
    }

    public void setArtist(ArtistBean artist) {
        this.artist = artist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
