package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller grafico astratto, base per tutte le schermate del flusso di
 * login/selezione ruolo (RoleSelection, Login). Centralizza il riferimento al
 * Navigator e le azioni condivise dall'header/footer comuni a tutte le
 * schermate (cambio tema, aiuto), lasciando alle sottoclassi la gestione
 * della parte specifica di ciascuna schermata.
 */
public abstract class LoginGraphicController {

    private Navigator navigator;

    @FXML
    private Button themeToggleButton;

    @FXML
    private Button helpButton;

    public Navigator getNavigator() {
        return navigator;
    }

    /**
     * Impostato dal Navigator subito dopo il caricamento della FXML, prima di
     * invocare {@link #onNavigatedTo(Context)}.
     */
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    protected void onThemeToggle() {
        if (navigator != null) {
            navigator.toggleTheme();
        }
    }

    @FXML
    protected void onHelp() {
        if (navigator != null) {
            navigator.showHelp();
        }
    }

    /**
     * Invocato dal Navigator subito dopo aver caricato la schermata e dopo
     * {@link #setNavigator(Navigator)}, per permettere al controller di
     * inizializzarsi con l'eventuale contesto ricevuto dalla schermata
     * precedente (es. il ruolo scelto in RoleSelection). Implementazione di
     * default vuota: le sottoclassi la sovrascrivono quando necessitano del
     * contesto.
     */
    public void onNavigatedTo(Context context) {
        // no-op di default
    }
}
