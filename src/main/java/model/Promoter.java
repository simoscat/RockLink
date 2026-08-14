package model;

public class Promoter extends User {

    String role;
    Local local;

    public Promoter(String name, String surname, String email, Gender gender, Local local,
                    String role) {
        super(name, surname, email, gender);
        this.role = role;
        this.local = local;

        if (local != null) {
            this.local.addPromoter(this);
        }
    }

    public void changeLocal(Local local){
        this.local.removePromoter(this);
        this.local = local;
        this.local.addPromoter(this);
    }

}
