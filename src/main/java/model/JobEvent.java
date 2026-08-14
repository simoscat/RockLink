package model;

import java.util.List;
import java.util.Map;

public class JobEvent {

    String name;
    String address;
    String city;
    Map<String, String> contacts;
    List<Promoter> promoters;

    public JobEvent(String name, String address, String city, Map<String, String> contacts, List<Promoter> promoters){
        this.name = name;
        this.address = address;
        this.city = city;
        this.contacts = contacts;
        this.promoters = promoters;
    }

    public void addPromoter(Promoter promoter) {

        if (!this.promoters.contains(promoter)) { // ensures no duplicate promoters
            this.promoters.add(promoter);
        }

    }

    public void removePromoter(Promoter promoter){
        this.promoters.remove(promoter);
    }


}
