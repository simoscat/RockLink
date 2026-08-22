package view.login;

import engineering.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import view.GUIGraphicController;
import view.Navigator;
import view.NavigatorGUI;

/**
 * Controller della schermata di login (LoginView.fxml).
 * Ogni campo @FXML corrisponde a un fx:id definito nell'FXML;
 * ogni metodo @FXML corrisponde a un onAction="#nomeMetodo".
 */
public class LoginGraphicControllerGUI extends LoginGraphicController implements GUIGraphicController {

    @FXML
    private BorderPane rootPane;

    // ---- Pannello Musician ----
    @FXML
    private TextField musicianEmailField;
    @FXML
    private PasswordField musicianPasswordField;
    @FXML
    private Button musicianSignInButton;

    // ---- Pannello Promoter ----
    @FXML
    private TextField promoterEmailField;
    @FXML
    private PasswordField promoterPasswordField;
    @FXML
    private Button promoterSignInButton;

    // ---- Sign up ----
    @FXML
    private Button signUpMusicianButton;
    @FXML
    private Button signUpPromoterButton;

    private Parent view;
    @Override
    public void setView(Parent view) {
        this.view = view;
    }

    @Override
    public Parent getView() {
        return this.view;
    }


    public LoginGraphicControllerGUI(Navigator navigator) {
        super(navigator);
    }

    //Automatically called by FXMLLoader
    @FXML
    public void initialize() {
        musicianEmailField.requestFocus();
    }

    @Override
    public void start() {
        musicianEmailField.clear();
        musicianPasswordField.clear();
        promoterEmailField.clear();
        promoterPasswordField.clear();
    }

    @FXML
    private void handleMusicianSignIn(ActionEvent event) {
        this.setRole(Role.MUSICIAN);

        this.email = musicianEmailField.getText();
        this.password = musicianPasswordField.getText();

        if (!areFieldsValid(email, password)) {
            showAlert(AlertType.WARNING, "Campi mancanti", "Inserisci email e password.");
            return;
        }

        doLogin();
    }

    @FXML
    private void handlePromoterSignIn(ActionEvent event) {
        this.setRole(Role.PROMOTER);
        this.email = promoterEmailField.getText();
        this.password = promoterPasswordField.getText();

        if (!areFieldsValid(email, password)) {
            showAlert(AlertType.WARNING, "Campi mancanti", "Inserisci email e password.");
            return;
        }

        doLogin();
    }

    @FXML
    private void handleSignUpMusician(ActionEvent event) {
        musicianSignUp();
    }

    @FXML
    private void handleSignUpPromoter(ActionEvent event) {
        promoterSignUp();
    }


    private boolean areFieldsValid(String email, String password) {
        return email != null && !email.isBlank() && password != null && !password.isBlank();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showError(String message) {
        showAlert(AlertType.ERROR, "ERROR", message);
    }

    @Override
    public void showInfo(String message) {
        showAlert(AlertType.INFORMATION, "INFO", message);
    }

}