package view.promoterdashboard;

import bean.JobAnnouncementBean;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import view.GUIGraphicController;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PromoterDashboardGraphicControllerGUI extends PromoterDashboardGraphicController implements GUIGraphicController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private static final String CHEVRON_RIGHT = "M8,4 L16,12 L8,20 L10.5,20 L18.5,12 L10.5,4 Z";

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    public PromoterDashboardGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        populateUserCard();
        populateAnnouncements();
    }

    @FXML
    private BorderPane rootPane;

    // ---- User card ----
    @FXML
    private Label userAvatarLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userEmailLabel;

    // ---- Navigazione ----
    @FXML
    private HBox dashboardNavItem;
    @FXML
    private HBox createAnnouncementNavItem;
    @FXML
    private Button signOutButton;

    // ---- Annunci ----
    @FXML
    private Label announcementsCountLabel;
    @FXML
    private VBox announcementsContainer;

    private void populateUserCard() {
        if (userNameLabel == null) {
            return;
        }

        String name = navigator.getPromoter().getName();
        String surname = navigator.getPromoter().getSurname();

        userNameLabel.setText(name + " " + surname);
        userEmailLabel.setText(navigator.getPromoter().getEmail());
        userAvatarLabel.setText(initialsOf(name, surname));
    }

    private String initialsOf(String name, String surname) {
        String first = (name == null || name.isBlank()) ? "" : name.substring(0, 1);
        String second = (surname == null || surname.isBlank()) ? "" : surname.substring(0, 1);
        return (first + second).toUpperCase(Locale.ENGLISH);
    }

    private void populateAnnouncements() {
        announcementsContainer.getChildren().clear();

        List<JobAnnouncementBean> announcements = getPromoterJobAnnouncements();

        if (announcements == null || announcements.isEmpty()) {
            announcementsCountLabel.setText("0 published");

            Label emptyLabel = new Label("You haven't published any job announcements yet.");
            emptyLabel.getStyleClass().add("announcements-empty-state");
            announcementsContainer.getChildren().add(emptyLabel);
            return;
        }

        announcementsCountLabel.setText(announcements.size() + " published");

        for (JobAnnouncementBean announcement : announcements) {
            announcementsContainer.getChildren().add(buildAnnouncementCard(announcement));
        }
    }

    private HBox buildAnnouncementCard(JobAnnouncementBean announcement) {
        Region accent = new Region();
        accent.getStyleClass().addAll("card-accent", "accent-announcement");

        Label title = new Label(announcement.getTitle());
        title.getStyleClass().add("card-title");

        Label subtitle = new Label(subtitleFor(announcement));
        subtitle.getStyleClass().add("card-subtitle");

        VBox body = new VBox(4.0, title, subtitle);
        body.getStyleClass().add("card-body");
        HBox.setHgrow(body, Priority.ALWAYS);

        String status = announcement.getJobAnnouncementStatus();
        Label statusBadge = new Label("● " + status);
        statusBadge.getStyleClass().addAll("job-status-badge", jobStatusClassFor(status));

        SVGPath chevron = new SVGPath();
        chevron.setContent(CHEVRON_RIGHT);
        chevron.getStyleClass().add("announcement-chevron");
        chevron.setScaleX(0.667);
        chevron.setScaleY(0.667);

        VBox statusBox = new VBox(6.0, statusBadge, chevron);
        statusBox.setAlignment(Pos.CENTER_RIGHT);
        statusBox.getStyleClass().add("card-status");

        HBox card = new HBox(accent, body, statusBox);
        card.getStyleClass().add("application-card");
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(_ -> goToJobAnnouncement(announcement));

        return card;
    }

    private String subtitleFor(JobAnnouncementBean announcement) {
        String published = announcement.getPublishDate() != null ? announcement.getPublishDate().format(DATE_FORMAT) : "-";
        String event = announcement.getDate() != null ? announcement.getDate().format(DATE_FORMAT) : "-";
        String pay = announcement.getMoneyValue().getCurrency() + " " + announcement.getMoneyValue().getValue();

        return "Published " + published + "  ·  Event " + event + "  ·  " + pay;
    }

    private String jobStatusClassFor(String status) {
        return switch (status) {
            case "OPEN" -> "job-status-open";
            case "FILLED" -> "job-status-filled";
            default -> "job-status-closed";
        };
    }

    @FXML
    private void handleCreateAnnouncement() {
        goToCreateAnnouncement();
    }

    @FXML
    private void handleSignOut() {
        doLogout();
    }

    @FXML
    private void handleRefresh() {
        reloadDashboard();
    }

    @FXML
    private void handleNotifications() {
        viewNotifications();
    }

}