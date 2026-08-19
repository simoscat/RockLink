package view;

import javafx.stage.Stage;
import view.login.LoginGraphicControllerGUI;

public class NavigatorGUI extends Navigator {

    private LoginGraphicControllerGUI login;
    private Stage stage;


    public NavigatorGUI() {
        super();
        this.stage=new Stage();
        this.stage.setTitle("-- FitConnect --");
        this.stage.setMinWidth(600);
        this.stage.setMinHeight(400);
    }

    @Override
    public void startUp() {
        goToLogin();
    }

    @Override
    public void viewLogin() {
        if (this.login == null) { login = new LoginGraphicControllerGUI(this); }
        this.login.start(null);
    }

    @Override
    public void viewMusicianDashboard() {

    }

    @Override
    public void viewPromoterDashboard() {

    }

    @Override
    public void viewAnnouncementDetails() {

    }

    @Override
    public void viewAnnouncementApplications() {

    }

    @Override
    public void viewCreateAnnouncement() {

    }
}
