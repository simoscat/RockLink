package view.signup.promoter;

import javafx.scene.Parent;
import javafx.scene.control.*;
import view.GUIGraphicController;
import view.Navigator;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PromoterRegistrationGraphicControllerGUI extends PromoterRegistrationGraphicController implements GUIGraphicController {

    private Parent view;

    public PromoterRegistrationGraphicControllerGUI(Navigator n) {
        super(n);
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
        if (firstNameField == null) {
            return;
        }

        firstNameField.clear();
        lastNameField.clear();
        genderToggleGroup.selectToggle(null);
        emailField.clear();
        passwordField.clear();

        contactsContainer.getChildren().remove(1, contactsContainer.getChildren().size());
        contactRows.clear();
        contactTypeField.clear();
        contactValueField.clear();
        contactRows.add(new ContactRow(contactTypeField, contactValueField));

        firstNameField.requestFocus();
    }

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button backToLoginButton;

    // ---- Dati anagrafici ----
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    // ---- Genere ----
    @FXML
    private ToggleGroup genderToggleGroup;
    @FXML
    private ToggleButton genderMaleToggle;
    @FXML
    private ToggleButton genderFemaleToggle;
    @FXML
    private ToggleButton genderNotSpecifiedToggle;

    // ---- Credenziali ----
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    // ---- Contatti ----
    @FXML
    private ScrollPane contactsScrollPane;
    @FXML
    private VBox contactsContainer;

    // Campi della prima riga contatto (presente di default nell'FXML,
    // funge anche da template per le righe aggiunte a runtime).
    @FXML
    private TextField contactTypeField;
    @FXML
    private TextField contactValueField;

    @FXML
    private Button addContactButton;

    // ---- Azione principale ----
    @FXML
    private Button createAccountButton;

    // Righe contatto correnti (la prima è quella dichiarata nell'FXML,
    // le altre vengono aggiunte a runtime da handleAddContact).
    private final List<ContactRow> contactRows = new ArrayList<>();

    private record ContactRow(TextField typeField, TextField valueField) {
    }

    @FXML
    private void initialize() {
        genderMaleToggle.setUserData("MALE");
        genderFemaleToggle.setUserData("FEMALE");
        genderNotSpecifiedToggle.setUserData("NOT_SPECIFIED");

        contactRows.add(new ContactRow(contactTypeField, contactValueField));

        firstNameField.requestFocus();
    }

    @FXML
    private void handleBackToLogin() {
        navigator.goToLogin();
    }

    @FXML
    private void handleAddContact() {
        TextField typeField = new TextField();
        typeField.setPrefWidth(130.0);
        typeField.setPromptText("Type (e.g. Phone)");
        typeField.getStyleClass().add("text-field");

        TextField valueField = new TextField();
        valueField.setMaxWidth(Double.MAX_VALUE);
        valueField.setPromptText("Value (e.g. +1 555 123456)");
        valueField.getStyleClass().add("text-field");
        HBox.setHgrow(valueField, javafx.scene.layout.Priority.ALWAYS);

        HBox contactRow = new HBox(8.0, typeField, valueField);

        contactsContainer.getChildren().add(contactRow);
        contactRows.add(new ContactRow(typeField, valueField));
    }

    @FXML
    private void handleCreateAccount() {
        this.name = firstNameField.getText();
        this.surname = lastNameField.getText();
        this.email = emailField.getText();
        this.password = passwordField.getText();

        Toggle selectedGender = genderToggleGroup.getSelectedToggle();
        if (selectedGender == null) {
            showError("Please select your gender.");
            return;
        }
        this.gender = (String) selectedGender.getUserData();

        Map<String, String> collectedContacts = new LinkedHashMap<>();
        for (ContactRow row : contactRows) {
            String type = row.typeField().getText();
            String value = row.valueField().getText();

            boolean typeBlank = type == null || type.isBlank();
            boolean valueBlank = value == null || value.isBlank();

            if (typeBlank && valueBlank) {
                continue;
            }
            if (typeBlank || valueBlank) {
                showError("Please fill in both the type and the value for every contact.");
                return;
            }

            collectedContacts.put(type, value);
        }

        if (collectedContacts.isEmpty()) {
            showError("Please add at least one contact.");
            return;
        }

        this.contacts = collectedContacts;

        doRegistration();
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