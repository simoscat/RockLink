package bean;


public class SessionBean {

    private int id;
    private MusicianBean musician;
    private PromoterBean promoter;

    public SessionBean(int id, MusicianBean musician) {
        this.id = id;
        this.musician = musician;
    }

    public SessionBean(int id, PromoterBean promoter){
        this.id = id;
        this.promoter = promoter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MusicianBean getMusician() {
        return musician;
    }

    public void setMusician(MusicianBean musician) {
        this.musician = musician;
    }

    public PromoterBean getPromoter() {
        return promoter;
    }

    public void setPromoter(PromoterBean promoter) {
        this.promoter = promoter;
    }

}
