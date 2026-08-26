package view.login;

import engineering.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import view.GUIGraphicController;
import view.Navigator;


public class LoginGraphicControllerGUI extends LoginGraphicController implements GUIGraphicController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField musicianEmailField;
    @FXML
    private PasswordField musicianPasswordField;
    @FXML
    private Button musicianSignInButton;

    @FXML
    private TextField promoterEmailField;
    @FXML
    private PasswordField promoterPasswordField;
    @FXML
    private Button promoterSignInButton;

    @FXML
    private Button signUpMusicianButton;
    @FXML
    private Button signUpPromoterButton;

    @FXML
    private Button googleLoginButton;
    @FXML
    private Button spotifyLoginButton;

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
        if (musicianEmailField == null) {
            return;
        }

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
            navigator.showError("Insert email and password.");
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
            navigator.showError("Insert email and password.");
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

    @FXML
    private void handleGoogleLogin(ActionEvent event) {
        navigator.showInfo("Not implemented yet");
    }

    @FXML
    private void handleSpotifyLogin(ActionEvent event) {
        navigator.showInfo("Not implemented yet");
    }


    private boolean areFieldsValid(String email, String password) {
        return email != null && !email.isBlank() && password != null && !password.isBlank();
    }

}