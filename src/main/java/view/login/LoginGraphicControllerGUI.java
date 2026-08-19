package view.login;

import bean.InstrumentBean;
import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import controller.LoginController;
import engineering.EmailChecker;
import engineering.PasswordChecker;
import engineering.enums.Screen;
import exception.ControllerLogicException;
import exception.WrongCredentialsException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Navigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LoginGraphicControllerGUI extends LoginGraphicController {

    private static final String FXML_BASE_LOGIN = "/fxml/base_login.fxml";
    private static final String FXML_MUSICIAN_LOGIN = "/fxml/musician_login.fxml";
    private static final String FXML_PROMOTER_LOGIN = "/fxml/promoter_login.fxml";
    private static final String FXML_MUSICIAN_SIGNUP = "/fxml/musician_signup.fxml";
    private static final String FXML_PROMOTER_SIGNUP = "/fxml/promoter_signup.fxml";

    private static final String[] MASTERY_LEVELS = {"AMATEUR", "BEGINNER", "INTERMEDIATE", "EXPERIENCED", "MASTER"};
    private static final String[] MASTERY_LABELS = {"AMT", "BEG", "INT", "EXP", "MAS"};

    private final LoginController loginController = new LoginController();
    private Stage stage;

    public LoginGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start(Scanner scanner) {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Rock Link");
            stage.setOnCloseRequest(e -> Platform.exit());
        }
        showBaseLogin();
        stage.show();
    }

    // ---------- Screen loading ----------

    private Parent load(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load FXML: " + fxmlPath, e);
        }
    }

    private void setScene(Parent root) {
        Scene scene = stage.getScene();
        if (scene == null) {
            stage.setScene(new Scene(root));
        } else {
            scene.setRoot(root);
        }
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    private void showBaseLogin() {
        Parent root = load(FXML_BASE_LOGIN);

        ((Hyperlink) root.lookup("#musicianContinueLink")).setOnAction(e -> showMusicianLogin());
        ((Hyperlink) root.lookup("#promoterContinueLink")).setOnAction(e -> showPromoterLogin());
        ((Hyperlink) root.lookup("#loginLink")).setOnAction(e ->
                showInfo("Select Musician or Promoter above to log in."));

        setScene(root);
    }

    private void showMusicianLogin() {
        Parent root = load(FXML_MUSICIAN_LOGIN);

        TextField emailField = (TextField) root.lookup("#emailField");
        PasswordField passwordField = (PasswordField) root.lookup("#passwordField");

        ((Button) root.lookup("#backButton")).setOnAction(e -> showBaseLogin());
        ((Hyperlink) root.lookup("#signupLink")).setOnAction(e -> showMusicianSignup());
        ((Hyperlink) root.lookup("#forgotPasswordLink")).setOnAction(e ->
                showInfo("Please contact support to reset your password."));
        ((Button) root.lookup("#loginButton")).setOnAction(e ->
                musicianLogin(emailField.getText().trim(), passwordField.getText()));

        setScene(root);
    }

    private void showPromoterLogin() {
        Parent root = load(FXML_PROMOTER_LOGIN);

        TextField emailField = (TextField) root.lookup("#emailField");
        PasswordField passwordField = (PasswordField) root.lookup("#passwordField");

        ((Button) root.lookup("#backButton")).setOnAction(e -> showBaseLogin());
        ((Hyperlink) root.lookup("#signupLink")).setOnAction(e -> showPromoterSignup());
        ((Hyperlink) root.lookup("#forgotPasswordLink")).setOnAction(e ->
                showInfo("Please contact support to reset your password."));
        ((Button) root.lookup("#loginButton")).setOnAction(e ->
                promoterLogin(emailField.getText().trim(), passwordField.getText()));

        setScene(root);
    }

    private void showMusicianSignup() {
        Parent root = load(FXML_MUSICIAN_SIGNUP);

        TextField firstNameField = (TextField) root.lookup("#firstNameField");
        TextField lastNameField = (TextField) root.lookup("#lastNameField");
        TextField stageNameField = (TextField) root.lookup("#stageNameField");
        TextField emailField = (TextField) root.lookup("#emailField");
        PasswordField passwordField = (PasswordField) root.lookup("#passwordField");

        ToggleButton maleToggle = (ToggleButton) root.lookup("#genderMaleToggle");
        ToggleButton femaleToggle = (ToggleButton) root.lookup("#genderFemaleToggle");
        ToggleButton notSpecifiedToggle = (ToggleButton) root.lookup("#genderNotSpecifiedToggle");

        VBox instrumentsContainer = (VBox) root.lookup("#instrumentsContainer");

        List<InstrumentRow> instrumentRows = new ArrayList<>();
        instrumentRows.add(new InstrumentRow(
                (TextField) root.lookup("#instrumentNameField"),
                new ToggleButton[]{
                        (ToggleButton) root.lookup("#levelAmateurToggle"),
                        (ToggleButton) root.lookup("#levelBeginnerToggle"),
                        (ToggleButton) root.lookup("#levelIntermediateToggle"),
                        (ToggleButton) root.lookup("#levelExperiencedToggle"),
                        (ToggleButton) root.lookup("#levelMasterToggle")
                }
        ));

        ((Button) root.lookup("#addInstrumentButton")).setOnAction(e ->
                instrumentRows.add(addInstrumentRow(instrumentsContainer)));

        ((Button) root.lookup("#backButton")).setOnAction(e -> showBaseLogin());
        ((Hyperlink) root.lookup("#loginLink")).setOnAction(e -> showMusicianLogin());
        ((Button) root.lookup("#createAccountButton")).setOnAction(e -> musicianRegistration(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                stageNameField.getText().trim(),
                selectedGender(maleToggle, femaleToggle, notSpecifiedToggle),
                emailField.getText().trim(),
                passwordField.getText(),
                instrumentRows
        ));

        setScene(root);
    }

    private void showPromoterSignup() {
        Parent root = load(FXML_PROMOTER_SIGNUP);

        TextField firstNameField = (TextField) root.lookup("#firstNameField");
        TextField lastNameField = (TextField) root.lookup("#lastNameField");
        TextField emailField = (TextField) root.lookup("#emailField");
        PasswordField passwordField = (PasswordField) root.lookup("#passwordField");

        ToggleButton maleToggle = (ToggleButton) root.lookup("#genderMaleToggle");
        ToggleButton femaleToggle = (ToggleButton) root.lookup("#genderFemaleToggle");
        ToggleButton notSpecifiedToggle = (ToggleButton) root.lookup("#genderNotSpecifiedToggle");

        VBox contactsContainer = (VBox) root.lookup("#contactsContainer");

        List<ContactRow> contactRows = new ArrayList<>();
        contactRows.add(new ContactRow(
                (TextField) root.lookup("#contactTypeField"),
                (TextField) root.lookup("#contactValueField")
        ));

        ((Button) root.lookup("#addContactButton")).setOnAction(e ->
                contactRows.add(addContactRow(contactsContainer)));

        ((Button) root.lookup("#backButton")).setOnAction(e -> showBaseLogin());
        ((Hyperlink) root.lookup("#loginLink")).setOnAction(e -> showPromoterLogin());
        ((Button) root.lookup("#createAccountButton")).setOnAction(e -> promoterRegistration(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                selectedGender(maleToggle, femaleToggle, notSpecifiedToggle),
                emailField.getText().trim(),
                passwordField.getText(),
                contactRows
        ));

        setScene(root);
    }

    // ---------- Dynamic rows ----------

    private InstrumentRow addInstrumentRow(VBox container) {
        TextField nameField = new TextField();
        nameField.setPromptText("Instrument name (e.g. Guitar)");

        ToggleGroup group = new ToggleGroup();
        ToggleButton[] toggles = new ToggleButton[MASTERY_LABELS.length];
        HBox togglesRow = new HBox(8);

        for (int i = 0; i < MASTERY_LABELS.length; i++) {
            ToggleButton toggle = new ToggleButton(MASTERY_LABELS[i]);
            toggle.setToggleGroup(group);
            toggle.getStyleClass().add("toggle-pill");
            toggle.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(toggle, Priority.ALWAYS);
            togglesRow.getChildren().add(toggle);
            toggles[i] = toggle;
        }
        toggles[1].setSelected(true);

        VBox row = new VBox(10, nameField, togglesRow);
        row.getStyleClass().add("instrument-box");
        container.getChildren().add(row);

        return new InstrumentRow(nameField, toggles);
    }

    private ContactRow addContactRow(VBox container) {
        TextField typeField = new TextField();
        typeField.setPromptText("Type (e.g. Phone)");
        typeField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(typeField, Priority.ALWAYS);

        TextField valueField = new TextField();
        valueField.setPromptText("Value (e.g. +1 555 123 4567)");
        valueField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(valueField, Priority.ALWAYS);

        HBox row = new HBox(10, typeField, valueField);
        container.getChildren().add(row);

        return new ContactRow(typeField, valueField);
    }

    // ---------- Business logic ----------

    private void musicianLogin(String email, String password) {
        try {
            MusicianBean mb = new MusicianBean(email, password);
            SessionBean session = loginController.musicianLogIn(mb);
            showInfo("Login successful! Welcome " + session.getMusician().getName());
            navigator.setCurrentScreen(Screen.MUSICIAN_DASHBOARD);
            navigator.nextScreen();
        } catch (WrongCredentialsException | ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void promoterLogin(String email, String password) {
        try {
            PromoterBean pb = new PromoterBean(email, password);
            SessionBean session = loginController.promoterLogin(pb);
            showInfo("Login successful! Welcome " + session.getPromoter().getName());
            navigator.setCurrentScreen(Screen.PROMOTER_DASHBOARD);
            navigator.nextScreen();
        } catch (WrongCredentialsException | ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void musicianRegistration(String name, String surname, String stageName, String gender,
                                       String email, String password, List<InstrumentRow> rows) {

        if (name.isBlank() || surname.isBlank() || stageName.isBlank()) {
            showError("Please fill in your name, surname and stage name.");
            return;
        }

        if (gender == null) {
            showError("Please select a gender.");
            return;
        }

        if (!EmailChecker.isValidEmail(email)) {
            showError("Invalid email, please try again.");
            return;
        }

        if (!PasswordChecker.isPasswordValid(password)) {
            showError("Password cannot contain these characters: " + PasswordChecker.getInvalidCharacters());
            return;
        }

        List<InstrumentBean> instruments = new ArrayList<>();

        for (InstrumentRow row : rows) {
            String instrumentName = row.nameField.getText().trim();

            if (instrumentName.isBlank()) {
                continue;
            }

            String mastery = selectedMastery(row.levelToggles);

            if (mastery == null) {
                showError("Please select a mastery level for " + instrumentName + ".");
                return;
            }

            instruments.add(new InstrumentBean(instrumentName, mastery));
        }

        if (instruments.isEmpty()) {
            showError("Please add at least one instrument.");
            return;
        }

        try {
            MusicianBean mb = new MusicianBean(name, surname, email, gender, password, stageName, instruments);
            SessionBean session = loginController.musicianRegistration(mb);
            showInfo("Registration successful! Welcome " + session.getMusician().getName());
            navigator.setCurrentScreen(Screen.MUSICIAN_DASHBOARD);
            navigator.nextScreen();
        } catch (ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void promoterRegistration(String name, String surname, String gender, String email, String password,
                                       List<ContactRow> rows) {

        if (name.isBlank() || surname.isBlank()) {
            showError("Please fill in your name and surname.");
            return;
        }

        if (gender == null) {
            showError("Please select a gender.");
            return;
        }

        if (!EmailChecker.isValidEmail(email)) {
            showError("Invalid email, please try again.");
            return;
        }

        if (!PasswordChecker.isPasswordValid(password)) {
            showError("Password cannot contain these characters: " + PasswordChecker.getInvalidCharacters());
            return;
        }

        Map<String, String> contacts = new HashMap<>();

        for (ContactRow row : rows) {
            String type = row.typeField.getText().trim();
            String value = row.valueField.getText().trim();

            if (type.isBlank() || value.isBlank()) {
                continue;
            }

            contacts.put(type, value);
        }

        if (contacts.isEmpty()) {
            showError("Please add at least one contact.");
            return;
        }

        try {
            PromoterBean pb = new PromoterBean(name, surname, email, gender, password, contacts);
            SessionBean session = loginController.promoterRegistration(pb);
            showInfo("Registration successful! Welcome " + session.getPromoter().getName());
            navigator.setCurrentScreen(Screen.PROMOTER_DASHBOARD);
            navigator.nextScreen();
        } catch (ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // ---------- Helpers ----------

    private String selectedGender(ToggleButton male, ToggleButton female, ToggleButton notSpecified) {
        if (male.isSelected()) {
            return "MALE";
        }
        if (female.isSelected()) {
            return "FEMALE";
        }
        if (notSpecified.isSelected()) {
            return "NOT_SPECIFIED";
        }
        return null;
    }

    private String selectedMastery(ToggleButton[] toggles) {
        for (int i = 0; i < toggles.length; i++) {
            if (toggles[i].isSelected()) {
                return MASTERY_LEVELS[i];
            }
        }
        return null;
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Rock Link");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Rock Link");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private static final class InstrumentRow {
        private final TextField nameField;
        private final ToggleButton[] levelToggles;

        private InstrumentRow(TextField nameField, ToggleButton[] levelToggles) {
            this.nameField = nameField;
            this.levelToggles = levelToggles;
        }
    }

    private static final class ContactRow {
        private final TextField typeField;
        private final TextField valueField;

        private ContactRow(TextField typeField, TextField valueField) {
            this.typeField = typeField;
            this.valueField = valueField;
        }
    }

}