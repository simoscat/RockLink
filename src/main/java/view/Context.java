package view;

import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;

public class Context {
    
    private SessionBean session;
    private MusicianBean musician;
    private PromoterBean promoter;

    public Context(){}

    public Context(MusicianBean musician){
        this.musician = musician;
    }

    public Context(PromoterBean promoter){
        this.promoter = promoter;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setSession(SessionBean session) {
        this.session = session;
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
