package view;

import javafx.fxml.FXML;

/**
 * Controller grafico della schermata "Benvenuto" (RoleSelection.fxml), in cui
 * l'utente sceglie se accedere come Musicista o come Promoter.
 */
public class RoleSelectionController extends LoginGraphicController {

    @FXML
    private void handleMusicianContinue() {
        goToLogin(Role.MUSICIAN);
    }

    @FXML
    private void handlePromoterContinue() {
        goToLogin(Role.PROMOTER);
    }

    @FXML
    private void handleLoginLink() {
        // In questa schermata non è ancora stato scelto un ruolo: si propone
        // di default l'accesso musicista, essendo la prima opzione mostrata.
        goToLogin(Role.MUSICIAN);
    }

    private void goToLogin(Role role) {
        if (getNavigator() != null) {
            getNavigator().navigateTo(Screen.LOGIN, new Context(role));
        }
    }
}
