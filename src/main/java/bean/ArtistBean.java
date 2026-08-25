package bean;

import engineering.enums.ArtistType;

import java.util.Map;

public class ArtistBean {
    private String artistName;
    private ArtistType type;
    private String email;
    private Map<String, String> details;


    public ArtistBean(String artistName, ArtistType type, String email, Map<String, String> details) {
        this.artistName = artistName;
        this.type = type;
        this.email = email;
        this.details = details;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public ArtistType getType() {
        return type;
    }

    public void setType(ArtistType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

}
