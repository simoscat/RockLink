package model;

import engineering.enums.Gender;

import java.util.Map;

public class Promoter extends User {

    private Map<String, String> contacts;

    // create promoter without job announcements
    public Promoter(String name, String surname, String email, Gender gender, Map<String, String> contacts) {
        super(name, surname, email, gender);
        this.contacts = contacts;
    }

    public Map<String, String> howToContact() {
        return contacts;
    }
}
