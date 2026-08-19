package bean;

import java.util.List;
import java.util.Map;

public class PromoterBean extends UserBean{

    private Map<String, String> contacts;

    public PromoterBean(String name, String surname, String email, String gender, String password,
                        Map<String, String> contacts) {
        super(name, surname, email, gender, password);
        this.contacts = contacts;
    }

    public PromoterBean(String email, String password) {
        super(email, password);
    }


    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }
}
