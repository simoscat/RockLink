package view;

import bean.*;
import engineering.SessionManager;
import engineering.enums.Screen;

import java.util.List;

public abstract class Navigator {
    
    private Screen currentScreen;
    private Context context;
    private Screen previousScreen;

    // context retrieving and setting

    public SessionBean getSession(){
        return this.context.getSession();
    }

    public void setSession(SessionBean session){
        this.context.setSession(session);
    }

    public MusicianBean getMusician(){
        return context.getMusician();
    }

    public PromoterBean getPromoter(){
        return context.getPromoter();
    }

    public void setMusician(MusicianBean musician){
        this.context.setMusician(musician);
    }

    public void setPromoter(PromoterBean promoter){
        this.context.setPromoter(promoter);
    }

    public List<NotificationBean> getNotifications(){
        return this.context.getNotifications();
    }

    public void setNotifications(List<NotificationBean> notifications){
        this.context.setNotifications(notifications);
    }

    public void setCurrentJobAnnouncement(JobAnnouncementBean currentJobAnnouncement) {
        this.context.setCurrentJobAnnouncement(currentJobAnnouncement);
    }

    public JobAnnouncementBean getCurrentJobAnnouncement() {
        return this.context.getCurrentJobAnnouncement();
    }

    public List<JobAnnouncementBean> getJobAnnouncements(){
        return this.context.getJobAnnouncements();
    }

    public void setJobAnnouncements(List<JobAnnouncementBean> jobAnnouncements){
        this.context.setJobAnnouncements(jobAnnouncements);
    }

    public void addJobAnnouncement(JobAnnouncementBean jobAnnouncement){
        this.context.addJobAnnouncement(jobAnnouncement);
    }

    public void setCurrentJobApplication(JobApplicationBean currentJobApplication) {
        this.context.setCurrentJobApplication(currentJobApplication);
    }

    public JobApplicationBean getCurrentJobApplication() {
        return this.context.getCurrentJobApplication();
    }

    public List<JobApplicationBean> getJobApplications(){
        return this.context.getJobApplications();
    }

    public void setJobApplications(List<JobApplicationBean> jobApplications){
        this.context.setJobApplications(jobApplications);
    }

    public void addJobApplication(JobApplicationBean jobApplication){
        this.context.addJobApplication(jobApplication);
    }

    // navigation

    protected Navigator() {
        this.context = new Context();
    }

    public void restart(){
        if (this.context.getSession() != null){
            SessionManager.getInstance().deleteSession(context.getSession().getId());
        }
        this.context = new Context();
        this.currentScreen = null;
        logout();
        goToLogin();
    }

    protected abstract void logout();

    public void setCurrentScreen(Screen screen) {
        this.currentScreen = screen;
    }
    
    public void nextScreen(){
        
        if (currentScreen == null){
            return;
        }
        
        switch(currentScreen){
            
            case LOGIN -> viewLogin();
            case PROMOTER_REGISTRATION -> viewPromoterRegistration();
            case MUSICIAN_REGISTRATION -> viewMusicianRegistration();
            case MUSICIAN_DASHBOARD -> viewMusicianDashboard();
            case PROMOTER_DASHBOARD -> viewPromoterDashboard();
            case VIEW_ANNOUNCEMENT_DETAILS -> viewAnnouncementDetails();
            case VIEW_ANNOUNCEMENT_APPLICATIONS -> viewAnnouncementApplications();
            case VIEW_APPLICATION_DETAILS -> viewApplicationDetails();
            case CREATE_ANNOUNCEMENT -> viewCreateAnnouncement();
            case OPEN_ANNOUNCEMENTS_DISCOVERY -> viewOpenAnnouncementsDiscovery();
            case NOTIFICATIONS -> viewNotifications();
            
        }

    }

    private void setPreviousScreen(){ previousScreen = currentScreen; }

    public void goToOpenAnnouncementsDiscovery(){ setPreviousScreen(); setCurrentScreen(Screen.OPEN_ANNOUNCEMENTS_DISCOVERY);
        viewOpenAnnouncementsDiscovery();}

    public void goToLogin(){
        setCurrentScreen(Screen.LOGIN);
        viewLogin();
    }
    public void goToMusicianDashboard(){
        setCurrentScreen(Screen.MUSICIAN_DASHBOARD);
        viewMusicianDashboard();
    }
    public void goToPromoterDashboard(){
        setCurrentScreen(Screen.PROMOTER_DASHBOARD);
        viewPromoterDashboard();
    }
    public void goToAnnouncementDetails(){

        if (this.currentScreen == Screen.NOTIFICATIONS || this.currentScreen == Screen.MUSICIAN_DASHBOARD
        || this.currentScreen == Screen.PROMOTER_DASHBOARD || this.currentScreen == Screen.OPEN_ANNOUNCEMENTS_DISCOVERY){
            setPreviousScreen();
        }

        setCurrentScreen(Screen.VIEW_ANNOUNCEMENT_DETAILS);
        viewAnnouncementDetails();
    }
    public void goToAnnouncementApplications(){
        setCurrentScreen(Screen.VIEW_ANNOUNCEMENT_APPLICATIONS);
        viewAnnouncementApplications();
    }
    public void goToCreateAnnouncement(){
        setCurrentScreen(Screen.CREATE_ANNOUNCEMENT);
        viewCreateAnnouncement();
    }
    public void goToPromoterRegistration(){
        setCurrentScreen(Screen.PROMOTER_REGISTRATION);
        viewPromoterRegistration();
    }
    public void goToMusicianRegistration(){
        setCurrentScreen(Screen.MUSICIAN_REGISTRATION);
        viewMusicianRegistration();
    }
    public void goToApplicationDetails(){
        setCurrentScreen(Screen.VIEW_APPLICATION_DETAILS);
        viewApplicationDetails();
    }

    public void goToNotifications(){
        if (this.currentScreen == Screen.MUSICIAN_DASHBOARD || this.currentScreen == Screen.PROMOTER_DASHBOARD
        || this.currentScreen == Screen.OPEN_ANNOUNCEMENTS_DISCOVERY){
            setPreviousScreen();
        }
        setCurrentScreen(Screen.NOTIFICATIONS);
        viewNotifications();
    }

    public void goBack(){
        setCurrentScreen(previousScreen);
        nextScreen();
    }


    public abstract void startUp();
    public abstract void viewLogin();
    public abstract void viewMusicianDashboard();
    public abstract void viewPromoterDashboard();
    public abstract void viewAnnouncementDetails();
    public abstract void viewAnnouncementApplications();
    public abstract void viewCreateAnnouncement();
    public abstract void viewPromoterRegistration();
    public abstract void viewMusicianRegistration();
    public abstract void viewApplicationDetails();
    public abstract void viewOpenAnnouncementsDiscovery();
    public abstract void viewNotifications();

    public void close() {
        if (this.getSession() != null){
            SessionManager.getInstance().deleteSession(this.getSession().getId());
        }
        System.exit(0);
    }

    public abstract void showError(String message);
    public abstract void showInfo(String message);
}
