package view.announcementapplications;

import bean.JobApplicationBean;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Artist;
import view.GUIGraphicController;
import view.Navigator;

import java.util.List;

public class AnnouncementApplicationsGraphicControllerGUI extends AnnouncementApplicationsGraphicController implements GUIGraphicController {

    private static final String ROW_ACTION_BTN_CLASS = "row-action-btn";

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    public AnnouncementApplicationsGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        populateApplications();
    }

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button backButton;
    @FXML
    private VBox applicationsContainer;

    private void populateApplications() {
        if (applicationsContainer == null) {
            return;
        }

        applicationsContainer.getChildren().clear();

        List<JobApplicationBean> applications = getJobApplications();

        if (applications == null || applications.isEmpty()) {
            Label emptyLabel = new Label("No job applications found.");
            emptyLabel.getStyleClass().add("applications-empty-state");
            applicationsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (JobApplicationBean application : applications) {
            applicationsContainer.getChildren().add(buildApplicationRow(application));
        }
    }

    private HBox buildApplicationRow(JobApplicationBean application) {
        Artist artist = application.getArtist();

        Region accent = new Region();
        accent.getStyleClass().addAll("card-accent", "accent-announcement");

        Label name = new Label(artist.getArtistName());
        name.getStyleClass().add("applicant-name");

        Label email = new Label(artist.getEmail());
        email.getStyleClass().add("applicant-email");

        Label offer = new Label(application.getRaiseOffer() != null ? "Counter offer: +" + application.getRaiseOffer() : "Counter offer: -");
        offer.getStyleClass().add("applicant-offer");

        VBox body = new VBox(4.0, name, email, offer);
        body.getStyleClass().add("card-body");
        HBox.setHgrow(body, Priority.ALWAYS);

        String status = application.getStatus();
        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().addAll("status-badge", statusClassFor(status));

        Button detailsButton = new Button("DETAILS");
        detailsButton.getStyleClass().add(ROW_ACTION_BTN_CLASS);
        detailsButton.setOnAction(_ -> {
            navigator.setCurrentJobApplication(application);
            jumpToJobApplication();
        });

        Button acceptButton = new Button("ACCEPT");
        acceptButton.getStyleClass().addAll(ROW_ACTION_BTN_CLASS, "accept-btn");
        acceptButton.setDisable(!"PENDING".equals(status));
        acceptButton.setOnAction(_ -> {
            navigator.setCurrentJobApplication(application);
            acceptJobApplication();
        });

        Button rejectButton = new Button("REJECT");
        rejectButton.getStyleClass().addAll(ROW_ACTION_BTN_CLASS, "reject-btn");
        rejectButton.setDisable(!"PENDING".equals(status));
        rejectButton.setOnAction(_ -> {
            navigator.setCurrentJobApplication(application);
            rejectJobApplication();
        });

        HBox actionsRow = new HBox(8.0, detailsButton, acceptButton, rejectButton);

        VBox statusBox = new VBox(8.0, statusBadge, actionsRow);
        statusBox.setAlignment(Pos.CENTER_RIGHT);
        statusBox.getStyleClass().add("card-status");

        HBox row = new HBox(accent, body, statusBox);
        row.getStyleClass().add("application-card");

        return row;
    }

    private String statusClassFor(String status) {
        return switch (status) {
            case "ACCEPTED" -> "status-confirmed";
            case "REJECTED" -> "status-rejected";
            default -> "status-pending";
        };
    }

    @FXML
    private void handleBack() {
        backToJobAnnouncement();
    }

    @FXML
    private void handleRefresh() {
        refreshUI();
    }

}