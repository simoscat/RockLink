package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.applicationdetail.JobApplicationDetailGraphicControllerGUI;
import view.login.LoginGraphicControllerGUI;
import view.musiciandashboard.MusicianDashboardGraphicControllerGUI;
import view.openannouncementsdiscovery.OpenAnnouncementsDiscoveryGraphicControllerGUI;
import view.promoterdashboard.PromoterDashboardGraphicControllerGUI;
import view.jobannouncementdetails.JobAnnouncementDetailsGraphicControllerGUI;
import view.announcementapplications.AnnouncementApplicationsGraphicControllerGUI;
import view.createannouncement.CreateAnnouncementGraphicControllerGUI;
import view.signup.musician.MusicianRegistrationGraphicControllerGUI;
import view.signup.promoter.PromoterRegistrationGraphicControllerGUI;

import java.io.IOException;

public class NavigatorGUI extends Navigator {

    /** Tutti i file FXML vivono in src/main/resources/fxml/ */
    private static final String FXML_DIR = "/fxml/";

    private LoginGraphicControllerGUI login;

    private MusicianRegistrationGraphicControllerGUI musicianRegistration;
    private PromoterRegistrationGraphicControllerGUI promoterRegistration;

    private MusicianDashboardGraphicControllerGUI musicianDashboard;
    private PromoterDashboardGraphicControllerGUI promoterDashboard;

    private JobAnnouncementDetailsGraphicControllerGUI announcementDetails;
    private AnnouncementApplicationsGraphicControllerGUI announcementApplications;
    private CreateAnnouncementGraphicControllerGUI createAnnouncement;

    private JobApplicationDetailGraphicControllerGUI applicationDetails;
    private OpenAnnouncementsDiscoveryGraphicControllerGUI announcementDiscovery;

    private final Stage stage;


    public NavigatorGUI() {
        super();
        this.stage = new Stage();
        this.stage.setTitle("<< ROCKLINK >>");
        this.stage.setMinWidth(800);
        this.stage.setMinHeight(600);
    }

    public Stage getStage() {
        return this.stage;
    }

    @Override
    protected void logout() {
        this.login = null;
        this.musicianRegistration = null;
        this.promoterRegistration = null;
        this.musicianDashboard = null;
        this.promoterDashboard = null;
        this.announcementDetails = null;
        this.announcementApplications = null;
        this.createAnnouncement = null;
        this.applicationDetails = null;
        this.announcementDiscovery = null;
    }

    @Override
    public void startUp() {
        goToLogin();
    }

    @Override
    public void viewLogin() {

        if (this.login == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "LoginView.fxml"));
                this.login = new LoginGraphicControllerGUI(this);
                loader.setController(this.login);
                Parent root = loader.load();
                this.login.setView(root);
                this.login.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("LoginView.fxml");
            }
        }

        this.login.start();
        this.showScreen(this.login.getView());


    }

    @Override
    public void viewMusicianDashboard() {

        if (this.musicianDashboard == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "MusicianDashboardView.fxml"));
                this.musicianDashboard = new MusicianDashboardGraphicControllerGUI(this);
                loader.setController(this.musicianDashboard);
                Parent root = loader.load();
                this.musicianDashboard.setView(root);
                this.musicianDashboard.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("MusicianDashboardView.fxml");
            }
        }

        this.musicianDashboard.start();
        this.showScreen(this.musicianDashboard.getView());

    }

    @Override
    public void viewPromoterDashboard() {

        if (this.promoterDashboard == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "PromoterDashboardView.fxml"));
                this.promoterDashboard = new PromoterDashboardGraphicControllerGUI(this);
                loader.setController(this.promoterDashboard);
                Parent root = loader.load();
                this.promoterDashboard.setView(root);
                this.promoterDashboard.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("PromoterDashboardView.fxml");
            }
        }

        this.promoterDashboard.start();
        this.showScreen(this.promoterDashboard.getView());

    }

    @Override
    public void viewAnnouncementDetails() {

        if (this.announcementDetails == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "JobAnnouncementDetailsView.fxml"));
                this.announcementDetails = new JobAnnouncementDetailsGraphicControllerGUI(this);
                loader.setController(this.announcementDetails);
                Parent root = loader.load();
                this.announcementDetails.setView(root);
                this.announcementDetails.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("JobAnnouncementDetailsView.fxml");
            }
        }

        this.announcementDetails.start();
        this.showScreen(this.announcementDetails.getView());

    }

    @Override
    public void viewAnnouncementApplications() {

        if (this.announcementApplications == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "AnnouncementApplicationsView.fxml"));
                this.announcementApplications = new AnnouncementApplicationsGraphicControllerGUI(this);
                loader.setController(this.announcementApplications);
                Parent root = loader.load();
                this.announcementApplications.setView(root);
                this.announcementApplications.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("AnnouncementApplicationsView.fxml");
            }
        }

        this.announcementApplications.start();
        this.showScreen(this.announcementApplications.getView());

    }

    @Override
    public void viewCreateAnnouncement() {

        if (this.createAnnouncement == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "CreateAnnouncementView.fxml"));
                this.createAnnouncement = new CreateAnnouncementGraphicControllerGUI(this);
                loader.setController(this.createAnnouncement);
                Parent root = loader.load();
                this.createAnnouncement.setView(root);
                this.createAnnouncement.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("CreateAnnouncementView.fxml");
            }
        }

        this.createAnnouncement.start();
        this.showScreen(this.createAnnouncement.getView());

    }

    @Override
    public void viewPromoterRegistration() {

        if (this.promoterRegistration == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "PromoterRegistrationView.fxml"));
                this.promoterRegistration = new PromoterRegistrationGraphicControllerGUI(this);
                loader.setController(this.promoterRegistration);
                Parent root = loader.load();
                this.promoterRegistration.setView(root);
                this.promoterRegistration.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("PromoterRegistrationView.fxml");
            }
        }

        this.promoterRegistration.start();
        this.showScreen(this.promoterRegistration.getView());

    }

    @Override
    public void viewMusicianRegistration() {

        if (this.musicianRegistration == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "MusicianRegistrationView.fxml"));
                this.musicianRegistration = new MusicianRegistrationGraphicControllerGUI(this);
                loader.setController(this.musicianRegistration);
                Parent root = loader.load();
                this.musicianRegistration.setView(root);
                this.musicianRegistration.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("MusicianRegistrationView.fxml");
            }
        }

        this.musicianRegistration.start();
        this.showScreen(this.musicianRegistration.getView());

    }

    @Override
    public void viewApplicationDetails() {

        if (this.applicationDetails == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "JobApplicationDetailView.fxml"));
                this.applicationDetails = new JobApplicationDetailGraphicControllerGUI(this);
                loader.setController(this.applicationDetails);
                Parent root = loader.load();
                this.applicationDetails.setView(root);
                this.applicationDetails.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("JobApplicationDetailView.fxml");
            }
        }

        this.applicationDetails.start();
        this.showScreen(this.applicationDetails.getView());

    }

    @Override
    public void viewOpenAnnouncementsDiscovery() {

        if (this.announcementDiscovery == null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_DIR + "OpenAnnouncementsDiscoveryView.fxml"));
                this.announcementDiscovery = new OpenAnnouncementsDiscoveryGraphicControllerGUI(this);
                loader.setController(this.announcementDiscovery);
                Parent root = loader.load();
                this.announcementDiscovery.setView(root);
                this.announcementDiscovery.setNavigatorGUI(this);

            } catch (IOException _) {
                graphicsError("OpenAnnouncementsDiscoveryView.fxml");
            }
        }

        this.announcementDiscovery.start();
        this.showScreen(this.announcementDiscovery.getView());

    }

    public void graphicsError(String file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("GRAPHICS ERROR");
        alert.setHeaderText(null);
        alert.setContentText("Can't find the setup file " + file);
        alert.showAndWait();
    }

    private void showScreen(Parent view){
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(view);
            stage.setScene(scene);
        } else {
            scene.setRoot(view);
        }
        stage.show();
    }
}