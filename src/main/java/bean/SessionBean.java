package bean;

import model.Musician;
import model.Promoter;

public class SessionBean {

    private int id;
    private Musician musician;
    private Promoter promoter;

    public SessionBean(int id, Musician musician) {
        this.id = id;
        this.musician = musician;
    }

    public SessionBean(int id, Promoter promoter){
        this.id = id;
        this.promoter = promoter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(Musician musician) {
        this.musician = musician;
    }

    public Promoter getPromoter() {
        return promoter;
    }

    public void setPromoter(Promoter promoter) {
        this.promoter = promoter;
    }

}
