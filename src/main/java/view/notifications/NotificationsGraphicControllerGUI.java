package view.notifications;

import bean.NotificationBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import view.GUIGraphicController;
import view.Navigator;

import java.util.List;

public class NotificationsGraphicControllerGUI extends NotificationsGraphicController implements GUIGraphicController {

    private static final String ROW_ACTION_BTN_CLASS = "row-action-btn";

    private Parent view;

    public NotificationsGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

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
        populateNotifications();
    }

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button backButton;
    @FXML
    private VBox notificationsContainer;

    private void populateNotifications() {

        if (notificationsContainer == null) {
            return;
        }

        notificationsContainer.getChildren().clear();

        List<NotificationBean> notifications = getNotifications();

        if (notifications == null || notifications.isEmpty()) {
            Label emptyLabel = new Label("No notifications found.");
            emptyLabel.getStyleClass().add("notifications-empty-state");
            notificationsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (NotificationBean notification : notifications) {
            notificationsContainer.getChildren().add(buildNotificationRow(notification));
        }

    }

    private HBox buildNotificationRow(NotificationBean notification) {

        Region accent = new Region();
        accent.getStyleClass().addAll("card-accent", "accent-notification");

        Label sender = new Label(notification.getSender());
        sender.getStyleClass().add("notification-sender");

        Label time = new Label(notification.getTime().format(DTF));
        time.getStyleClass().add("notification-time");

        Label body = new Label(buildBody(notification));
        body.getStyleClass().add("notification-body");
        body.setWrapText(true);

        VBox rowBody = new VBox(4.0, sender, time, body);
        rowBody.getStyleClass().add("card-body");
        HBox.setHgrow(rowBody, Priority.ALWAYS);

        Button viewJobButton = new Button("VIEW JOB");
        viewJobButton.getStyleClass().add(ROW_ACTION_BTN_CLASS);
        viewJobButton.setOnAction(_ -> {
            navigator.setCurrentJobAnnouncement(notification.getJobAnnouncement());
            goToJobAnnouncement();
        });

        VBox actionsBox = new VBox(viewJobButton);
        actionsBox.getStyleClass().add("card-status");

        HBox row = new HBox(accent, rowBody, actionsBox);
        row.getStyleClass().add("application-card");

        return row;

    }

    @FXML
    private void handleBack() {
        backToDashboard();
    }

    @FXML
    private void handleRefresh() {
        populateNotifications();
    }

}