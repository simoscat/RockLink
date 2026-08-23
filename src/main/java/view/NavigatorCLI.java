package view;

import view.applicationdetail.JobApplicationDetailGraphicControllerCLI;
import view.login.LoginGraphicControllerCLI;
import view.musiciandashboard.MusicianDashboardGraphicControllerCLI;
import view.openannouncementsdiscovery.OpenAnnouncementsDiscoveryGraphicControllerCLI;
import view.promoterdashboard.PromoterDashboardGraphicControllerCLI;
import view.jobannouncementdetails.JobAnnouncementDetailsGraphicControllerCLI;
import view.announcementapplications.AnnouncementApplicationsGraphicControllerCLI;
import view.createannouncement.CreateAnnouncementGraphicControllerCLI;
import view.signup.musician.MusicianRegistrationGraphicControllerCLI;
import view.signup.promoter.PromoterRegistrationGraphicControllerCLI;

public class NavigatorCLI extends Navigator {

    private LoginGraphicControllerCLI login;

    private MusicianRegistrationGraphicControllerCLI musicianRegistration;
    private PromoterRegistrationGraphicControllerCLI promoterRegistration;

    private MusicianDashboardGraphicControllerCLI musicianDashboard;
    private PromoterDashboardGraphicControllerCLI promoterDashboard;

    private JobAnnouncementDetailsGraphicControllerCLI announcementDetails;
    private AnnouncementApplicationsGraphicControllerCLI announcementApplications;
    private CreateAnnouncementGraphicControllerCLI createAnnouncement;

    private JobApplicationDetailGraphicControllerCLI applicationDetails;
    private OpenAnnouncementsDiscoveryGraphicControllerCLI announcementDiscovery;


    @Override
    protected void logout(){
        this.login = null;
        this.musicianRegistration = null;
        this.promoterRegistration = null;
        this.musicianDashboard = null;
        this.promoterDashboard = null;
        this.announcementDetails = null;
        this.announcementApplications = null;
        this.createAnnouncement = null;
    }

    @Override
    public void startUp() {
        goToLogin();
    }

    @Override
    public void viewLogin() {
        if (this.login == null) { this.login = new LoginGraphicControllerCLI(this);}
        this.login.start();
    }

    @Override
    public void viewMusicianDashboard() {
        if (this.musicianDashboard == null) {
            this.musicianDashboard = new MusicianDashboardGraphicControllerCLI(this);
        }
        this.musicianDashboard.start();
    }

    @Override
    public void viewPromoterDashboard() {
        if (this.promoterDashboard == null) { this.promoterDashboard = new PromoterDashboardGraphicControllerCLI(this); }
        this.promoterDashboard.start();
    }


    @Override
    public void viewAnnouncementDetails() {
        if (this.announcementDetails == null) { this.announcementDetails = new JobAnnouncementDetailsGraphicControllerCLI(this); }
        this.announcementDetails.start();
    }

    @Override
    public void viewAnnouncementApplications() {
        if (this.announcementApplications == null) { this.announcementApplications = new AnnouncementApplicationsGraphicControllerCLI(this); }
        this.announcementApplications.start();
    }

    @Override
    public void viewCreateAnnouncement() {
        if (this.createAnnouncement == null) { this.createAnnouncement = new CreateAnnouncementGraphicControllerCLI(this); }
        this.createAnnouncement.start();
    }

    @Override
    public void viewPromoterRegistration() {
        PromoterRegistrationGraphicControllerCLI controller = this.promoterRegistration;
        if (controller == null) {
            controller = new PromoterRegistrationGraphicControllerCLI(this);
            this.promoterRegistration = controller;
        }
        controller.start();
    }

    @Override
    public void viewMusicianRegistration() {
        if (this.musicianRegistration == null) { this.musicianRegistration = new MusicianRegistrationGraphicControllerCLI(this); }
        this.musicianRegistration.start();
    }

    @Override
    public void viewApplicationDetails() {
        if (this.applicationDetails == null) { this.applicationDetails = new JobApplicationDetailGraphicControllerCLI(this); }
        this.applicationDetails.start();
    }

    @Override
    public void viewOpenAnnouncementsDiscovery() {
        if (this.announcementDiscovery == null) { this.announcementDiscovery = new OpenAnnouncementsDiscoveryGraphicControllerCLI(this); }
        this.announcementDiscovery.start();
    }

    @Override
    public void showError(String message) {
        System.out.println("[ERROR] "+message);
    }

    @Override
    public void showInfo(String message) {
        System.out.println("[INFO] "+message);
    }
}
