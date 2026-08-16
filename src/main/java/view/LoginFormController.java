package view;

import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import controller.LoginController;
import exception.ControllerLogicException;
import exception.WrongCredentialsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller grafico della schermata "Bentornato" (Login.fxml), condivisa sia
 * per l'accesso Musicista sia per l'accesso Promoter: l'aspetto (badge,
 * sottotitolo, colore accento) e il comportamento del login vengono
 * parametrizzati in base al {@link Role} ricevuto tramite
 * {@link #onNavigatedTo(Context)}.
 */
public class LoginFormController extends LoginGraphicController {

    private final LoginController loginController = new LoginController();

    private Role role = Role.MUSICIAN;

    @FXML
    private Button backButton;

    @FXML
    private Label roleBadgeLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private VBox loginCard;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private void initialize() {
        // Aspetto di default in attesa dell'eventuale contesto passato dal Navigator
        applyRole(Role.MUSICIAN);
    }

    @Override
    public void onNavigatedTo(Context context) {
        Role receivedRole = (context != null) ? context.getData(Role.class) : null;
        applyRole(receivedRole != null ? receivedRole : Role.MUSICIAN);
    }

    private void applyRole(Role role) {
        this.role = role;
        hideError();
        emailField.clear();
        passwordField.clear();

        loginCard.getStyleClass().removeAll("accent-musician", "accent-promoter");
        roleBadgeLabel.getStyleClass().removeAll("role-badge-musician", "role-badge-promoter");
        loginButton.getStyleClass().removeAll("btn-musician", "btn-promoter");
        registerLink.getStyleClass().removeAll("link-musician", "link-promoter");

        if (role == Role.PROMOTER) {
            roleBadgeLabel.setText("PROMOTER");
            subtitleLabel.setText("Accedi al tuo account promoter");
            emailField.setPromptText("es. booking@venue.it");
            loginCard.getStyleClass().add("accent-promoter");
            roleBadgeLabel.getStyleClass().add("role-badge-promoter");
            loginButton.getStyleClass().add("btn-promoter");
            registerLink.getStyleClass().add("link-promoter");
        } else {
            roleBadgeLabel.setText("MUSICISTA");
            subtitleLabel.setText("Accedi al tuo account musicista");
            emailField.setPromptText("es. marco.rossi@email.it");
            loginCard.getStyleClass().add("accent-musician");
            roleBadgeLabel.getStyleClass().add("role-badge-musician");
            loginButton.getStyleClass().add("btn-musician");
            registerLink.getStyleClass().add("link-musician");
        }
    }

    @FXML
    private void handleBack() {
        if (getNavigator() != null) {
            getNavigator().goBack();
        }
    }

    @FXML
    private void handleLogin() {
        hideError();

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            showError("Inserisci email e password.");
            return;
        }

        try {
            SessionBean session;
            Screen destination;

            if (role == Role.PROMOTER) {
                session = loginController.promoterLogin(new PromoterBean(email, password));
                destination = Screen.PROMOTER_HOME;
            } else {
                session = loginController.musicianLogIn(new MusicianBean(email, password));
                destination = Screen.MUSICIAN_HOME;
            }

            if (getNavigator() != null) {
                // La propagazione della SessionBean alla schermata successiva
                // (es. salvataggio in SessionManager/Context) sarà completata
                // dall'implementazione concreta del Navigator.
                getNavigator().navigateTo(destination, new Context(session));
            }
        } catch (WrongCredentialsException | ControllerLogicException e) {
            showError("Email o password non corretti.");
        }
    }

    @FXML
    private void handleForgotPassword() {
        if (getNavigator() != null) {
            getNavigator().navigateTo(Screen.FORGOT_PASSWORD, new Context(role));
        }
    }

    @FXML
    private void handleRegister() {
        if (getNavigator() != null) {
            getNavigator().navigateTo(Screen.REGISTER, new Context(role));
        }
    }

    private void showError(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setVisible(true);
        loginErrorLabel.setManaged(true);
    }

    private void hideError() {
        loginErrorLabel.setText("");
        loginErrorLabel.setVisible(false);
        loginErrorLabel.setManaged(false);
    }
}
