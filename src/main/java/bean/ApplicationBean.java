package bean;

import model.Artist;

public abstract class ApplicationBean {
    
    private Artist artist;
    private String status;

    public ApplicationBean() {
    }

    public ApplicationBean(Artist artist, String status) {
        this.artist = artist;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
