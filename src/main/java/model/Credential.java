package model;

public class Credential {

    private String email;
    private String cryptPassword;

    public Credential(String email, String cryptPassword) {
        this.email = email;
        this.cryptPassword = cryptPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getCryptPassword() {
        return cryptPassword;
    }

}
