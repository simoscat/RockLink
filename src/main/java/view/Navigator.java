package view;

import engineering.enums.Screen;

public abstract class Navigator {
    
    private Screen currentScreen;
    private Context context;
    
    protected Navigator() {
        this.context = new Context();
    }
    
    public void setCurrentScreen(Screen screen) {
        this.currentScreen = screen;
    }
    
    public void nextScreen(){
        
        if (currentScreen == null){
            return;
        }
        
        switch(currentScreen){
            
            case LOGIN -> viewLogin();
            case MUSICIAN_DASHBOARD -> viewMusicianDashboard();
            case PROMOTER_DASHBOARD -> viewPromoterDashboard();
            case VIEW_ANNOUNCEMENT_DETAILS -> viewAnnouncementDetails();
            case VIEW_ANNOUNCEMENT_APPLICATIONS -> viewAnnouncementApplications();
            case VIEW_MY_ANNOUNCEMENTS -> viewMyAnnouncements();
            case CREATE_ANNOUNCEMENT -> createAnnouncement();
            
        }
        
    }

    public abstract void viewLogin();
    public abstract void viewMusicianDashboard();
    public abstract void viewPromoterDashboard();
    public abstract void viewAnnouncementDetails();
    public abstract void viewAnnouncementApplications();
    public abstract void viewMyAnnouncements();
    public abstract void createAnnouncement();

}
