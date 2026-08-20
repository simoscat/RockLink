package view;

import bean.ApplicationBean;
import view.applicationDetail.JobApplicationDetailGraphicControllerCLI;
import view.login.LoginGraphicControllerCLI;
import view.musicianDashboard.MusicianDashboardGraphicControllerCLI;
import view.openAnnouncementsDiscovery.OpenAnnouncementsDiscoveryGraphicControllerCLI;
import view.promoterDashboard.PromoterDashboardGraphicControllerCLI;
import view.jobAnnouncementDetails.JobAnnouncementDetailsGraphicControllerCLI;
import view.announcementApplications.AnnouncementApplicationsGraphicControllerCLI;
import view.createAnnouncement.CreateAnnouncementGraphicControllerCLI;
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
        if (this.login == null) { login = new LoginGraphicControllerCLI(this);}
        this.login.start();
    }

    @Override
    public void viewMusicianDashboard() {
        if (this.musicianDashboard == null) {
            musicianDashboard = new MusicianDashboardGraphicControllerCLI(this);
        }
        this.musicianDashboard.start();
    }

    @Override
    public void viewPromoterDashboard() {
        if (this.promoterDashboard == null) { promoterDashboard = new PromoterDashboardGraphicControllerCLI(this); }
        this.promoterDashboard.start();
    }


    @Override
    public void viewAnnouncementDetails() {
        if (this.announcementDetails == null) { announcementDetails = new JobAnnouncementDetailsGraphicControllerCLI(this); }
        this.announcementDetails.start();
    }

    @Override
    public void viewAnnouncementApplications() {
        if (this.announcementApplications == null) { announcementApplications = new AnnouncementApplicationsGraphicControllerCLI(this); }
        this.announcementApplications.start();
    }

    @Override
    public void viewCreateAnnouncement() {
        if (this.createAnnouncement == null) { createAnnouncement = new CreateAnnouncementGraphicControllerCLI(this); }
        this.createAnnouncement.start();
    }

    @Override
    public void viewPromoterRegistration() {
        if (this.promoterRegistration == null) { promoterRegistration = new PromoterRegistrationGraphicControllerCLI(this); }
        this.promoterRegistration.start();
    }

    @Override
    public void viewMusicianRegistration() {
        if (this.musicianRegistration == null) { musicianRegistration = new MusicianRegistrationGraphicControllerCLI(this); }
        this.musicianRegistration.start();
    }

    @Override
    public void viewApplicationDetails() {
        if (this.applicationDetails == null) { applicationDetails = new JobApplicationDetailGraphicControllerCLI(this); }
        this.applicationDetails.start();
    }

    @Override
    public void viewOpenAnnouncementsDiscovery() {
        if (this.announcementDiscovery == null) { announcementDiscovery = new OpenAnnouncementsDiscoveryGraphicControllerCLI(this); }
        this.announcementDiscovery.start();
    }
}
