package model;

import engineering.EmailChecker;

public class Credential {

    private String email;
    private String cryptPassword;

    public Credential(String email, String cryptPassword) {
        if (!EmailChecker.isValidEmail(email)){
            throw new IllegalArgumentException("Invalid email: "+email);
        }
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
