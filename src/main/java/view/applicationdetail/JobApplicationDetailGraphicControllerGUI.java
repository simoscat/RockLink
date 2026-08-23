package view.applicationdetail;

import bean.JobApplicationBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.GUIGraphicController;
import view.Navigator;

import java.util.Map;

public class JobApplicationDetailGraphicControllerGUI extends JobApplicationDetailGraphicController implements GUIGraphicController {

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    public JobApplicationDetailGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        populateApplication();
    }

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button backButton;
    @FXML
    private Label statusBadge;
    @FXML
    private Label offerLabel;
    @FXML
    private VBox detailsContainer;
    @FXML
    private Button rejectButton;
    @FXML
    private Button acceptButton;

    private void populateApplication() {
        if (statusBadge == null) {
            return;
        }

        JobApplicationBean application = navigator.getCurrentJobApplication();

        String status = application.getStatus();
        statusBadge.setText(status);
        statusBadge.getStyleClass().removeAll("status-confirmed", "status-pending", "status-rejected");
        statusBadge.getStyleClass().add(statusClassFor(status));

        offerLabel.setText("Counter offer: +" + (application.getRaiseOffer() != null ? application.getRaiseOffer() : "0"));

        boolean pending = "PENDING".equals(status);
        acceptButton.setDisable(!pending);
        rejectButton.setDisable(!pending);

        detailsContainer.getChildren().clear();
        Map<String, String> details = application.getArtist().getArtistDetails();
        for (Map.Entry<String, String> entry : details.entrySet()) {
            detailsContainer.getChildren().add(buildDetailRow(entry.getKey(), entry.getValue()));
        }
    }

    private HBox buildDetailRow(String key, String value) {
        Label keyLabel = new Label(key + ":");
        keyLabel.getStyleClass().add("applicant-detail-key");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("applicant-detail-value");

        return new HBox(8.0, keyLabel, valueLabel);
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
        backToJobApplications();
    }

    @FXML
    private void handleAccept() {
        acceptApplication();
    }

    @FXML
    private void handleReject() {
        rejectApplication();
    }

}