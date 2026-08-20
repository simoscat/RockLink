package view;

import view.login.LoginGraphicController;
import view.login.LoginGraphicControllerCLI;
import view.musicianDashboard.MusicianDashboardGraphicControllerCLI;
import view.promoterDashboard.PromoterDashboardGraphicControllerCLI;
import view.announcementDetails.AnnouncementDetailsGraphicControllerCLI;
import view.announcementApplications.AnnouncementApplicationsGraphicControllerCLI;
import view.createAnnouncement.CreateAnnouncementGraphicControllerCLI;
import view.signup.musician.MusicianRegistrationGraphicControllerCLI;
import view.signup.promoter.PromoterRegistrationGraphicControllerCLI;

import java.util.Scanner;

public class NavigatorCLI extends Navigator {

    private LoginGraphicControllerCLI login;

    private MusicianRegistrationGraphicControllerCLI musicianRegistration;
    private PromoterRegistrationGraphicControllerCLI promoterRegistration;

    private MusicianDashboardGraphicControllerCLI musicianDashboard;
    private PromoterDashboardGraphicControllerCLI promoterDashboard;

    private AnnouncementDetailsGraphicControllerCLI announcementDetails;
    private AnnouncementApplicationsGraphicControllerCLI announcementApplications;
    private CreateAnnouncementGraphicControllerCLI createAnnouncement;


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
        if (this.announcementDetails == null) { announcementDetails = new AnnouncementDetailsGraphicControllerCLI(this); }
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
}
