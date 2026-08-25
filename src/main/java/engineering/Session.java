package engineering;

import model.Musician;
import model.Promoter;
//TODO VEDI SE RIMUOVERE IL CONCETTO DI SESSIONE
public class Session {
    private final int id;
    private Musician musician;
    private Promoter promoter;

    public Session(int id, Musician musician) {
        this.id = id;
        this.musician = musician;
    }

    public Session(int id, Promoter promoter){
        this.id = id;
        this.promoter = promoter;
    }

    public int getId() {
        return id;
    }
    public Musician getMusician() {
        return musician;
    }

    public Promoter getPromoter(){
        return promoter;
    }
}
