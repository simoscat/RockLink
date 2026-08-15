package model;

import engineering.enums.ArtistType;

import java.util.List;

public class Band implements Artist {
    String name;
    String description;
    List<Musician> members;
    Musician leader;
    String mainCity;


    public Band(String name, String description, Musician leader, List<Musician> members, String mainCity){
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.mainCity = mainCity;
        this.members = members;
    }

    public void addMember(Musician m){
        this.members.add(m);
    }

    public void removeMember(Musician m){
        this.members.remove(m);
    }

    public void moveCity(String city){
        this.mainCity = city;
    }


    @Override
    public String getArtistName() {
        return this.name;
    }

    @Override
    public ArtistType getType() {
        return ArtistType.BAND;
    }
}
