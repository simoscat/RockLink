package view;

/**
 * Contratto per la navigazione tra le schermate grafiche dell'applicazione.
 * <p>
 * Questa classe è astratta perché rappresenta soltanto le operazioni che le
 * schermate (e i relativi controller grafici) possono richiedere: la logica
 * concreta di caricamento delle FXML, cambio di Scene/Stage, gestione dello
 * storico per il tasto "indietro", gestione del tema e dell'help sarà
 * implementata in una sottoclasse concreta (es. una StageNavigator basata su
 * JavaFX Stage) in una fase successiva del progetto.
 */
public abstract class Navigator {

    /**
     * Naviga verso la schermata indicata, senza passare alcun contesto.
     */
    public abstract void navigateTo(Screen screen);

    /**
     * Naviga verso la schermata indicata, passando un contesto (es. il ruolo
     * scelto, oppure la sessione ottenuta dopo un login riuscito) che il
     * controller grafico di destinazione potrà leggere tramite
     * {@link LoginGraphicController#onNavigatedTo(Context)}.
     */
    public abstract void navigateTo(Screen screen, Context context);

    /**
     * Torna alla schermata precedente nello storico di navigazione.
     */
    public abstract void goBack();

    /**
     * Alterna tra tema chiaro e scuro dell'applicazione.
     */
    public abstract void toggleTheme();

    /**
     * Mostra la schermata/popup di aiuto.
     */
    public abstract void showHelp();

}
