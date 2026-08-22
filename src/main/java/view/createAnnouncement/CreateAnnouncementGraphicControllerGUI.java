package view.createAnnouncement;

import bean.JobAnnouncementBean;
import bean.MoneyValueBean;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementTag;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import view.GUIGraphicController;
import view.Navigator;
import view.NavigatorGUI;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateAnnouncementGraphicControllerGUI extends CreateAnnouncementGraphicController implements GUIGraphicController {

    private NavigatorGUI navigatorGUI;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
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

    public CreateAnnouncementGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        populateUserCard();
        resetForm();
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

    // ---- Form ----
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField eventDateField;
    @FXML
    private TextField eventTimeField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField payAmountField;

    @FXML
    private ToggleGroup currencyToggleGroup;
    @FXML
    private ToggleButton currencyEurToggle;
    @FXML
    private ToggleButton currencyUsdToggle;

    @FXML
    private ToggleButton tagLongTimeContractToggle;
    @FXML
    private ToggleButton tagNegotiableSalaryToggle;
    @FXML
    private ToggleButton tagUrgentToggle;
    @FXML
    private ToggleButton tagExpertsOnlyToggle;

    @FXML
    private Button cancelButton;
    @FXML
    private Button publishButton;

    /**
     * Chiamato in automatico da FXMLLoader subito dopo l'iniezione dei campi @FXML.
     * Associa a ogni ToggleButton "a scelta fissa" il valore enum che rappresenta,
     * seguendo lo stesso schema di MusicianRegistrationGraphicControllerGUI.
     */
    @FXML
    private void initialize() {
        currencyEurToggle.setUserData(CurrencyType.EUR.name());
        currencyUsdToggle.setUserData(CurrencyType.USD.name());

        tagLongTimeContractToggle.setUserData(JobAnnouncementTag.LONG_TIME_CONTRACT);
        tagNegotiableSalaryToggle.setUserData(JobAnnouncementTag.NEGOTIABLE_SALARY);
        tagUrgentToggle.setUserData(JobAnnouncementTag.URGENT);
        tagExpertsOnlyToggle.setUserData(JobAnnouncementTag.EXPERTS_ONLY);
    }

    private void populateUserCard() {
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

    private void resetForm() {
        titleField.clear();
        descriptionField.clear();
        eventDateField.clear();
        eventTimeField.clear();
        addressField.clear();
        payAmountField.clear();

        currencyToggleGroup.selectToggle(currencyEurToggle);

        for (ToggleButton tagToggle : List.of(tagLongTimeContractToggle, tagNegotiableSalaryToggle,
                tagUrgentToggle, tagExpertsOnlyToggle)) {
            tagToggle.setSelected(false);
        }

        titleField.requestFocus();
    }

    @FXML
    private void handleDashboard() {
        backToDashboard();
    }

    @FXML
    private void handleSignOut() {
        navigator.restart();
    }

    @FXML
    private void handleCancel() {
        backToDashboard();
    }

    @FXML
    private void handlePublish() {
        String title = titleField.getText();
        if (title == null || title.isBlank()) {
            showError("Please enter a title.");
            return;
        }

        String content = descriptionField.getText();
        if (content == null || content.isBlank()) {
            showError("Please enter a description.");
            return;
        }

        LocalDateTime eventDate = readEventDateTime();
        if (eventDate == null) {
            return;
        }

        String address = addressField.getText();
        if (address == null || address.isBlank()) {
            showError("Please enter the event address.");
            return;
        }

        MoneyValueBean moneyValue = readMoneyValue();
        if (moneyValue == null) {
            return;
        }

        List<JobAnnouncementTag> tags = readTags();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean(
                title,
                content,
                eventDate,
                navigator.getPromoter(),
                moneyValue,
                address,
                tags
        );

        publishAnnouncement(jobAnnouncementBean);
    }

    private LocalDateTime readEventDateTime() {
        String date = eventDateField.getText() == null ? "" : eventDateField.getText().trim();
        String time = eventTimeField.getText() == null ? "" : eventTimeField.getText().trim();

        try {
            LocalDateTime eventDateTime = LocalDateTime.parse(date + "T" + time);

            if (eventDateTime.isBefore(LocalDateTime.now())) {
                showError("The date and time cannot be in the past.");
                return null;
            }

            return eventDateTime;

        } catch (DateTimeParseException e) {
            showError("Invalid date/time. Use yyyy-MM-dd for the date and HH:mm for the time.");
            return null;
        }
    }

    private MoneyValueBean readMoneyValue() {
        BigDecimal amount;

        try {
            amount = new BigDecimal(payAmountField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Please enter a valid pay amount.");
            return null;
        }

        if (amount.signum() <= 0) {
            showError("Pay amount must be greater than zero.");
            return null;
        }

        Toggle selectedCurrency = currencyToggleGroup.getSelectedToggle();
        if (selectedCurrency == null) {
            showError("Please select a currency.");
            return null;
        }

        return new MoneyValueBean((String) selectedCurrency.getUserData(), amount);
    }

    private List<JobAnnouncementTag> readTags() {
        List<JobAnnouncementTag> tags = new ArrayList<>();

        for (ToggleButton tagToggle : List.of(tagLongTimeContractToggle, tagNegotiableSalaryToggle,
                tagUrgentToggle, tagExpertsOnlyToggle)) {

            if (tagToggle.isSelected()) {
                tags.add((JobAnnouncementTag) tagToggle.getUserData());
            }
        }

        return tags;
    }

    @Override
    protected void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "ERROR", message);
    }

    @Override
    protected void showInfo(String message) {
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