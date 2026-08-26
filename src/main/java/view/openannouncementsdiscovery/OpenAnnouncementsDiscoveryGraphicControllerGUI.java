package view.openannouncementsdiscovery;

import bean.JobAnnouncementBean;
import bean.JobApplicationBean;
import engineering.enums.JobAnnouncementTag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import view.GUIGraphicController;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class OpenAnnouncementsDiscoveryGraphicControllerGUI extends OpenAnnouncementsDiscoveryGraphicController implements GUIGraphicController {

    private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter EVENT_TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private static final String CHEVRON_RIGHT = "M8,4 L16,12 L8,20 L10.5,20 L18.5,12 L10.5,4 Z";
    private static final String PIN = "M6.7,8 A5.3,5.3 0 1,0 17.3,8 A5.3,5.3 0 1,0 6.7,8 Z M6.5,10 L17.5,10 L12,22 Z";
    private static final String CALENDAR = "M6,1 L8,1 L8,5.3 L6,5.3 Z M16,1 L18,1 L18,5.3 L16,5.3 Z M3,5 L21,5 L21,21 L3,21 Z "
            + "M6,11 L9,11 L9,14 L6,14 Z M10.5,11 L13.5,11 L13.5,14 L10.5,14 Z M15,11 L18,11 L18,14 L15,14 Z";
    private static final String CLOCK = "M3,12 A9,9 0 1,0 21,12 A9,9 0 1,0 3,12 Z M11.4,7 L12.6,7 L12.6,12 L11.4,12 Z M12,11.4 L17,11.4 L17,12.6 L12,12.6 Z";
    private static final String JOB_META_ICON_CLASS = "job-meta-icon";
    private static final String NOT_IMPLEMENTED_YET_MESSAGE = "Not implemented yet";

    public OpenAnnouncementsDiscoveryGraphicControllerGUI(Navigator navigator) {
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

    /** true = ultimo filtro applicato "All", false = "Open" (default, come findOpenJobAnnouncements()). */
    private boolean showingAll = false;

    @Override
    public void start() {
        populateUserCard();
        populateJobs();
    }

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label userAvatarLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userArtistNameLabel;
    @FXML
    private Label userEmailLabel;

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

    @FXML
    private ToggleButton openFilterToggle;
    @FXML
    private ToggleButton allFilterToggle;
    @FXML
    private TilePane jobsContainer;

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

    private void populateJobs() {
        openFilterToggle.setSelected(!showingAll);
        allFilterToggle.setSelected(showingAll);

        jobsContainer.getChildren().clear();

        List<JobAnnouncementBean> jobs = findOpenJobAnnouncements();

        if (jobs == null || jobs.isEmpty()) {
            Label emptyLabel = new Label("No job announcements found.");
            emptyLabel.getStyleClass().add("jobs-empty-state");
            jobsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (JobAnnouncementBean job : jobs) {
            jobsContainer.getChildren().add(buildJobCard(job));
        }
    }

    private HBox buildJobCard(JobAnnouncementBean job) {
        Region accent = new Region();
        accent.getStyleClass().addAll("card-accent", "accent-job");

        HBox topRow = new HBox(8.0, jobBadge(), spacer(), jobStatusBadge(job));
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(job.getTitle());
        title.getStyleClass().add("job-title");

        HBox locationRow = new HBox(6.0, icon(PIN, JOB_META_ICON_CLASS, 0.583), metaLabel(job.getAddress()));
        locationRow.setAlignment(Pos.CENTER_LEFT);

        HBox dateRow = new HBox(6.0, icon(CALENDAR, JOB_META_ICON_CLASS, 0.583), metaLabel(job.getDate().format(EVENT_DATE_FORMAT)));
        dateRow.setAlignment(Pos.CENTER_LEFT);
        HBox timeRow = new HBox(6.0, icon(CLOCK, JOB_META_ICON_CLASS, 0.583), metaLabel(job.getDate().format(EVENT_TIME_FORMAT)));
        timeRow.setAlignment(Pos.CENTER_LEFT);
        HBox dateTimeRow = new HBox(16.0, dateRow, timeRow);
        dateTimeRow.setAlignment(Pos.CENTER_LEFT);

        Label price = new Label(job.getMoneyValue().getCurrency() + " " + job.getMoneyValue().getValue());
        price.getStyleClass().add("job-price");

        HBox bottomRow = new HBox(8.0, price, spacer(), applicationIndicator(job));
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox body = new VBox(8.0, topRow, title, locationRow, dateTimeRow, spacer(), bottomRow);
        body.setPadding(new Insets(16.0));
        HBox.setHgrow(body, Priority.ALWAYS);

        HBox card = new HBox(accent, body);
        card.getStyleClass().add("job-card");
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(_ -> {
            navigator.setCurrentJobAnnouncement(job);
            goToJobAnnouncement();
        });

        return card;
    }

    private Label jobBadge() {
        Label badge = new Label("JOB");
        badge.getStyleClass().addAll("role-badge", "role-badge-promoter");
        return badge;
    }

    private Label jobStatusBadge(JobAnnouncementBean job) {
        String status = job.getJobAnnouncementStatus();
        Label badge = new Label("● " + status);
        badge.getStyleClass().addAll("job-status-badge", jobStatusClassFor(status));
        return badge;
    }

    private String jobStatusClassFor(String status) {
        return switch (status) {
            case "OPEN" -> "job-status-open";
            case "FILLED" -> "job-status-filled";
            default -> "job-status-closed";
        };
    }

    private Node applicationIndicator(JobAnnouncementBean job) {
        boolean applied = checkMusicianApplication(job);

        if (!applied) {
            return icon(CHEVRON_RIGHT, "job-chevron", 0.667);
        }

        JobApplicationBean myApplication = manageJobApplicationsController.findMusicianJobApplication(navigator.getMusician(), job);

        Label badge = new Label(myApplication.getStatus());
        badge.getStyleClass().addAll("status-badge", applicationStatusClassFor(myApplication.getStatus()));
        return badge;
    }

    private String applicationStatusClassFor(String status) {
        return switch (status) {
            case "ACCEPTED" -> "status-confirmed";
            case "REJECTED" -> "status-rejected";
            default -> "status-pending";
        };
    }

    private String tagsTextFor(List<JobAnnouncementTag> tags) {
        if (tags == null || tags.isEmpty()) {
            return "Open format";
        }

        StringBuilder sb = new StringBuilder();
        for (JobAnnouncementTag tag : tags) {
            if (!sb.isEmpty()) {
                sb.append(" / ");
            }
            sb.append(formatTag(tag));
        }
        return sb.toString();
    }

    private String formatTag(JobAnnouncementTag tag) {
        String[] words = tag.name().split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(word.charAt(0)).append(word.substring(1).toLowerCase(Locale.ENGLISH));
        }
        return sb.toString();
    }

    private Label metaLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("job-meta-text");
        return label;
    }

    private SVGPath icon(String content, String styleClass, double scale) {
        SVGPath path = new SVGPath();
        path.setContent(content);
        path.getStyleClass().add(styleClass);
        path.setScaleX(scale);
        path.setScaleY(scale);
        return path;
    }

    private Region spacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    @FXML
    private void handleDashboard() {
        backToMusicianDashboard();
    }

    @FXML
    private void handleDiscoverBands() {
        navigator.showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleMyBands() {
        navigator.showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleProfile() {
        navigator.showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleSettings() {
        navigator.showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleSignOut() {
        navigator.restart();
    }

    @FXML
    private void handleNotifications() {
        navigator.goToNotifications();
    }

    @FXML
    private void handleHelp() {
        navigator.showInfo(NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @FXML
    private void handleRefresh() {
        showingAll = false;
        refreshUI();
    }

    @FXML
    private void handleFilterOpen() {
        if (!openFilterToggle.isSelected()) {
            openFilterToggle.setSelected(true);
            return;
        }
        showingAll = false;
        openStart();
    }

    @FXML
    private void handleFilterAll() {
        if (!allFilterToggle.isSelected()) {
            allFilterToggle.setSelected(true);
            return;
        }
        showingAll = true;
        allStart();
    }

}
