package model;

import java.util.List;

public class Band {
    String name;
    List<Musician> members;
    Musician leader;
    String mainCity;


    public Band(String name, Musician leader, List<Musician> members, String mainCity){
        this.name = name;
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


}
