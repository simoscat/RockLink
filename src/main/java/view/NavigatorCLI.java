package view;

import view.login.LoginGraphicController;
import view.login.LoginGraphicControllerCLI;

import java.util.Scanner;

public class NavigatorCLI extends Navigator {

    private Scanner scanner = new Scanner(System.in);

    private LoginGraphicControllerCLI login;


    @Override
    public void startUp() {
        goToLogin();
    }

    @Override
    public void viewLogin() {
        if (this.login == null) { login = new LoginGraphicControllerCLI(this);}
        this.login.start(scanner);
    }

    @Override
    public void viewMusicianDashboard() {
        System.exit(0);
    }

    @Override
    public void viewPromoterDashboard() {
        System.exit(0);
    }


    @Override
    public void viewAnnouncementDetails() {

    }

    @Override
    public void viewAnnouncementApplications() {

    }

    @Override
    public void viewMyAnnouncements() {

    }

    @Override
    public void viewCreateAnnouncement() {

    }
}
