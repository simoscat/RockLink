package view.musiciandashboard;

import bean.JobApplicationBean;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import view.GUIGraphicController;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MusicianDashboardGraphicControllerGUI extends MusicianDashboardGraphicController implements GUIGraphicController {

    private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private static final String NOT_IMPLEMENTED_YET_MESSAGE = "Not implemented yet";


    public MusicianDashboardGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    @Override
    public void start() {
        populateUserCard();
        populateApplications();
    }


    @FXML
    private BorderPane rootPane;

    // ---- User card ----
    @FXML
    private Label userAvatarLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userArtistNameLabel;
    @FXML
    private Label userEmailLabel;

    // ---- Navigazione ----
    @FXML
    private HBox dashboardNavItem;
    @FXML
    private HBox discoverJobsNavItem;
    @FXML
    private HBox discoverBandsNavItem;
    @FXML
    private HBox myBandsNavItem;
    @FXML
    private HBox profileNavItem;
    @FXML
    private HBox settingsNavItem;
    @FXML
    private Button signOutButton;

    // ---- Applicazioni ----
    @FXML
    private Label applicationsCountLabel;
    @FXML
    private VBox applicationsContainer;

    private void populateUserCard() {
        if (userNameLabel == null) {
            return;
        }

        String name = navigator.getMusician().getName();
        String surname = navigator.getMusician().getSurname();

        userNameLabel.setText(name + " " + surname);
        userArtistNameLabel.setText(navigator.getMusician().getStageName());
        userEmailLabel.setText(navigator.getMusician().getEmail());
        userAvatarLabel.setText(initialsOf(name, surname));
    }

    private String initialsOf(String name, String surname) {
        String first = (name == null || name.isBlank()) ? "" : name.substring(0, 1);
        String second = (surname == null || surname.isBlank()) ? "" : surname.substring(0, 1);
        return (first + second).toUpperCase(Locale.ENGLISH);
    }

    private void populateApplications() {
        applicationsContainer.getChildren().clear();

        List<JobApplicationBean> applications = getApplications();

        if (applications == null || applications.isEmpty()) {
            applicationsCountLabel.setText("0 submitted");

            Label emptyLabel = new Label("You haven't submitted any job applications yet.");
            emptyLabel.getStyleClass().add("card-subtitle");
            applicationsContainer.getChildren().add(emptyLabel);
            return;
        }

        applicationsCountLabel.setText(applications.size() + " submitted");

        for (JobApplicationBean application : applications) {
            applicationsContainer.getChildren().add(buildApplicationCard(application));
        }
    }

    private HBox buildApplicationCard(JobApplicationBean application) {
        String status = application.getStatus();
        String accentClass = accentClassFor(status);
        String statusClass = statusClassFor(status);

        Region accent = new Region();
        accent.getStyleClass().addAll("card-accent", accentClass);

        Label title = new Label(application.getJobAnnouncementReference().getTitle());
        title.getStyleClass().add("card-title");

        Label subtitle = new Label(subtitleFor(application));
        subtitle.getStyleClass().add("card-subtitle");

        VBox body = new VBox(4.0, title, subtitle);
        body.getStyleClass().add("card-body");
        HBox.setHgrow(body, Priority.ALWAYS);

        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().addAll("status-badge", statusClass);

        VBox statusBox = new VBox(6.0, statusBadge);
        statusBox.setAlignment(Pos.CENTER_RIGHT);
        statusBox.getStyleClass().add("card-status");

        HBox card = new HBox(accent, body, statusBox);
        card.getStyleClass().add("application-card");
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(_ -> {
            navigator.setCurrentJobAnnouncement(application.getJobAnnouncementReference());
            goToJobAnnouncement();
        });

        return card;
    }

    private String subtitleFor(JobApplicationBean application) {
        String date = application.getJobAnnouncementReference().getDate().format(EVENT_DATE_FORMAT);
        String pay = application.getJobAnnouncementReference().getMoneyValue().getCurrency()
                + " " + application.getJobAnnouncementReference().getMoneyValue().getValue();

        if (application.getRaiseOffer() != null && application.getRaiseOffer().signum() > 0) {
            pay += " + " + application.getRaiseOffer() + " raise requested";
        }

        return date + "  ·  " + pay;
    }

    private String accentClassFor(String status) {
        return switch (status) {
            case "ACCEPTED" -> "accent-confirmed";
            case "REJECTED" -> "accent-rejected";
            default -> "accent-pending";
        };
    }

    private String statusClassFor(String status) {
        return switch (status) {
            case "ACCEPTED" -> "status-confirmed";
            case "REJECTED" -> "status-rejected";
            default -> "status-pending";
        };
    }

    @FXML
    private void handleDiscoverJobs() {
        viewOpenAnnouncements();
    }

    @FXML
    private void handleSignOut() {
        logout();
    }

    @FXML
    private void handleDiscoverBands(){
        showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleMyBands(){
        showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleProfile(){
        showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleSettings(){
        showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleNotifications(){
        showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleRefresh(){
        refreshDashboard();
    }

    @Override
    public void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "ERROR", message);
    }

    @Override
    public void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, "INFO", message);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}