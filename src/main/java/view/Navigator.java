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
            case CREATE_ANNOUNCEMENT -> viewCreateAnnouncement();
            
        }
        
    }

    public void goToLogin(){ setCurrentScreen(Screen.LOGIN); viewLogin();}
    public void goToMusicianDashboard(){ setCurrentScreen(Screen.MUSICIAN_DASHBOARD); viewMusicianDashboard();}
    public void goToPromoterDashboard(){ setCurrentScreen(Screen.PROMOTER_DASHBOARD); viewPromoterDashboard();}
    public void goToAnnouncementDetails(){ setCurrentScreen(Screen.VIEW_ANNOUNCEMENT_DETAILS); viewAnnouncementDetails();}
    public void goToAnnouncementApplications(){ setCurrentScreen(Screen.VIEW_ANNOUNCEMENT_APPLICATIONS); viewAnnouncementApplications();}
    public void goToMyAnnouncements(){ setCurrentScreen(Screen.VIEW_MY_ANNOUNCEMENTS); viewMyAnnouncements();}
    public void goToCreateAnnouncement(){ setCurrentScreen(Screen.CREATE_ANNOUNCEMENT); viewCreateAnnouncement();}

    public abstract void startUp();

    public abstract void viewLogin();
    public abstract void viewMusicianDashboard();
    public abstract void viewPromoterDashboard();
    public abstract void viewAnnouncementDetails();
    public abstract void viewAnnouncementApplications();
    public abstract void viewMyAnnouncements();
    public abstract void viewCreateAnnouncement();

}
