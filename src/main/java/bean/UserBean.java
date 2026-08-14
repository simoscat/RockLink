package bean;

public abstract class UserBean {
    private String name;
    private String surname;
    private String email;
    private String gender;
    private String password;

    protected UserBean(String name, String surname, String email, String gender, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }

    protected UserBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
