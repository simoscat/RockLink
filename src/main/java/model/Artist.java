package model;

import engineering.enums.ArtistType;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Artist {
    public String getArtistName();
    public ArtistType getType();
    public String getEmail();
    public Map<String, String> getArtistDetails();
}
