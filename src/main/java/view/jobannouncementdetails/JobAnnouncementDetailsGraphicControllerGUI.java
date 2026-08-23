package view.jobannouncementdetails;

import bean.JobAnnouncementBean;
import bean.JobApplicationBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Artist;
import view.GUIGraphicController;
import view.Navigator;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class JobAnnouncementDetailsGraphicControllerGUI extends JobAnnouncementDetailsGraphicController implements GUIGraphicController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy, HH:mm");

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    public JobAnnouncementDetailsGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        JobAnnouncementBean job = navigator.getCurrentJobAnnouncement();

        populateDetails(job);

        if (isMusician()) {
            configureMusicianView();
        } else {
            configurePromoterView();
        }
    }

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button backButton;

    @FXML
    private VBox panelTopBorder;
    @FXML
    private Label statusBadge;
    @FXML
    private Label hiredArtistBadge;
    @FXML
    private Label titleLabel;
    @FXML
    private Label addressValueLabel;
    @FXML
    private Label dateValueLabel;
    @FXML
    private Label publishedValueLabel;
    @FXML
    private Label payValueLabel;
    @FXML
    private Label contentLabel;
    @FXML
    private Label promoterNameLabel;
    @FXML
    private VBox contactsContainer;

    @FXML
    private VBox applicationSummaryBox;
    @FXML
    private Label applicationRaiseLabel;
    @FXML
    private Label applicationStatusLabel;

    @FXML
    private VBox applyFormBox;
    @FXML
    private TextField raiseOfferField;
    @FXML
    private Button applyButton;

    @FXML
    private HBox promoterActionsBox;
    @FXML
    private Button closeAnnouncementButton;
    @FXML
    private Button viewApplicationsButton;

    private void populateDetails(JobAnnouncementBean job) {
        if (statusBadge == null) {
            return;
        }

        String status = job.getJobAnnouncementStatus();
        statusBadge.setText("● " + status);
        statusBadge.getStyleClass().removeAll("job-status-open", "job-status-filled", "job-status-closed");
        statusBadge.getStyleClass().add(jobStatusClassFor(status));

        titleLabel.setText(job.getTitle());
        addressValueLabel.setText(job.getAddress());
        dateValueLabel.setText(job.getDate() != null ? job.getDate().format(DATE_FORMAT) : "-");
        publishedValueLabel.setText(job.getPublishDate() != null ? job.getPublishDate().format(DATE_FORMAT) : "-");
        payValueLabel.setText(job.getMoneyValue().getValue() + " " + job.getMoneyValue().getCurrency());
        contentLabel.setText(job.getContent());

        promoterNameLabel.setText(job.getPromoter().getName() + " " + job.getPromoter().getSurname());
        contactsContainer.getChildren().clear();
        for (Map.Entry<String, String> contact : job.getPromoter().getContacts().entrySet()) {
            Label contactLabel = new Label(contact.getKey() + ": " + contact.getValue());
            contactLabel.getStyleClass().add("contact-row");
            contactsContainer.getChildren().add(contactLabel);
        }

        Artist hiredArtist = job.getHiredArtist();
        boolean hasHiredArtist = hiredArtist != null;
        hiredArtistBadge.setVisible(hasHiredArtist);
        hiredArtistBadge.setManaged(hasHiredArtist);
        if (hasHiredArtist) {
            hiredArtistBadge.setText("Hired: " + hiredArtist.getArtistName() + " (" + hiredArtist.getType() + ")");
        }

        panelTopBorder.getStyleClass().removeAll("border-musician", "border-promoter");
        panelTopBorder.getStyleClass().add(isMusician() ? "border-musician" : "border-promoter");
    }

    private void configureMusicianView() {
        promoterActionsBox.setVisible(false);
        promoterActionsBox.setManaged(false);

        boolean applied = hasMusicianAlreadyApplied();

        applicationSummaryBox.setVisible(applied);
        applicationSummaryBox.setManaged(applied);

        if (applied) {
            JobApplicationBean application = getMusicianApplication();
            applicationRaiseLabel.setText("Your counter offer: +" + application.getRaiseOffer());
            applicationStatusLabel.setText(application.getStatus());
            applicationStatusLabel.getStyleClass().removeAll("status-confirmed", "status-pending", "status-rejected");
            applicationStatusLabel.getStyleClass().add(applicationStatusClassFor(application.getStatus()));
        }

        applyFormBox.setVisible(!applied);
        applyFormBox.setManaged(!applied);
        raiseOfferField.clear();
    }

    private void configurePromoterView() {
        applicationSummaryBox.setVisible(false);
        applicationSummaryBox.setManaged(false);
        applyFormBox.setVisible(false);
        applyFormBox.setManaged(false);

        promoterActionsBox.setVisible(true);
        promoterActionsBox.setManaged(true);
    }

    private String jobStatusClassFor(String status) {
        return switch (status) {
            case "OPEN" -> "job-status-open";
            case "FILLED" -> "job-status-filled";
            default -> "job-status-closed";
        };
    }

    private String applicationStatusClassFor(String status) {
        return switch (status) {
            case "ACCEPTED" -> "status-confirmed";
            case "REJECTED" -> "status-rejected";
            default -> "status-pending";
        };
    }

    @FXML
    private void handleBack() {

        if (isMusician()) {
            backToPreviousScreen();
        }
        else{
            backToPromoterDashboard();
        }

    }

    @FXML
    private void handleApply() {
        String raiseText = raiseOfferField.getText();
        BigDecimal raiseOffer = BigDecimal.ZERO;

        if (raiseText != null && !raiseText.isBlank()) {
            try {
                raiseOffer = new BigDecimal(raiseText.trim());
            } catch (NumberFormatException _) {
                navigator.showError("Invalid counter offer amount.");
                return;
            }

            if (raiseOffer.signum() < 0) {
                navigator.showError("The counter offer cannot be negative.");
                return;
            }
        }

        applyMusicianForJob(raiseOffer);
    }

    @FXML
    private void handleCloseAnnouncement() {
        closeJobAnnouncement();
    }

    @FXML
    private void handleViewApplications() {
        viewJobApplications();
    }

}