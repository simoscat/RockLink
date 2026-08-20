package view;

import javafx.stage.Stage;
//import view.login.LoginGraphicControllerGUI;
//import view.musicianDashboard.MusicianDashboardGraphicControllerGUI;
//import view.promoterDashboard.PromoterDashboardGraphicControllerGUI;
//import view.announcementDetails.AnnouncementDetailsGraphicControllerGUI;
//import view.announcementApplications.AnnouncementApplicationsGraphicControllerGUI;
//import view.createAnnouncement.CreateAnnouncementGraphicControllerGUI;

public class NavigatorGUI extends Navigator {

//    private LoginGraphicControllerGUI login;
//    private MusicianDashboardGraphicControllerGUI musicianDashboard;
//    private PromoterDashboardGraphicControllerGUI promoterDashboard;
//    private AnnouncementDetailsGraphicControllerGUI announcementDetails;
//    private AnnouncementApplicationsGraphicControllerGUI announcementApplications;
//    private CreateAnnouncementGraphicControllerGUI createAnnouncement;

    private Stage stage;


    public NavigatorGUI() {
        super();
        this.stage=new Stage();
        this.stage.setTitle("-- FitConnect --");
        this.stage.setMinWidth(600);
        this.stage.setMinHeight(400);
    }

    @Override
    protected void logout() {
        //TODO
    }

    @Override
    public void startUp() {
        goToLogin();
    }

    @Override
    public void viewLogin() {
        //TODO
    }

    @Override
    public void viewMusicianDashboard() {
        //TODO
    }

    @Override
    public void viewPromoterDashboard() {
        //TODO
    }

    @Override
    public void viewAnnouncementDetails() {
        //TODO
    }

    @Override
    public void viewAnnouncementApplications() {
        //TODO
    }

    @Override
    public void viewCreateAnnouncement() {
        //TODO
    }

    @Override
    public void viewPromoterRegistration() {
        //TODO
    }

    @Override
    public void viewMusicianRegistration() {
        //TODO
    }

    @Override
    public void viewApplicationDetails() {
        //TODO
    }

    @Override
    public void viewOpenAnnouncementsDiscovery() {
        //TODO
    }
}
