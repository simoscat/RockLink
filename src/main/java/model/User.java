package model;

import engineering.EmailChecker;

public abstract class User {
    private String name;
    private String surname;
    private String email;
    private Gender gender;

    protected User(String name, String surname, String email, Gender gender) {
        this.name = name;
        this.surname = surname;
        setEmail(email);
        this.gender = gender;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        if (!EmailChecker.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        this.email = email;
    }

    public Gender getGender(){
        return this.gender;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

}
