package view.signup.musician;

import bean.InstrumentBean;
import javafx.scene.Parent;
import javafx.scene.control.*;
import view.GUIGraphicController;
import view.Navigator;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MusicianRegistrationGraphicControllerGUI extends MusicianRegistrationGraphicController implements GUIGraphicController {

    private Parent view;

    public MusicianRegistrationGraphicControllerGUI(Navigator n) {
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
        stageNameField.clear();
        genderToggleGroup.selectToggle(null);
        emailField.clear();
        passwordField.clear();

        instrumentsContainer.getChildren().remove(1, instrumentsContainer.getChildren().size());
        instrumentRows.clear();
        instrumentNameField.clear();
        skillToggleGroup.selectToggle(skillBeginnerToggle);
        instrumentRows.add(new InstrumentRow(instrumentNameField, skillToggleGroup));

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
    @FXML
    private TextField stageNameField;

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

    // ---- Strumenti ----
    @FXML
    private ScrollPane instrumentsScrollPane;
    @FXML
    private VBox instrumentsContainer;

    // Campi della prima riga strumento (presente di default nell'FXML,
    // funge anche da template per le righe aggiunte a runtime).
    @FXML
    private TextField instrumentNameField;
    @FXML
    private ToggleGroup skillToggleGroup;
    @FXML
    private ToggleButton skillAmateurToggle;
    @FXML
    private ToggleButton skillBeginnerToggle;
    @FXML
    private ToggleButton skillIntermediateToggle;
    @FXML
    private ToggleButton skillExpertToggle;
    @FXML
    private ToggleButton skillMasterToggle;

    @FXML
    private Button addInstrumentButton;

    // ---- Azione principale ----
    @FXML
    private Button createAccountButton;

    // Righe strumento correnti (la prima è quella dichiarata nell'FXML,
    // le altre vengono aggiunte a runtime da handleAddInstrument).
    private final List<InstrumentRow> instrumentRows = new ArrayList<>();

    private record InstrumentRow(TextField nameField, ToggleGroup skillGroup) {
    }


    /**
     * Chiamato in automatico da FXMLLoader subito dopo l'iniezione dei campi @FXML.
     * Essendo l'FXML ricaricato da zero a ogni navigazione, questo metodo viene
     * rieseguito ogni volta che si arriva su questa schermata.
     */
    @FXML
    private void initialize() {
        genderMaleToggle.setUserData("MALE");
        genderFemaleToggle.setUserData("FEMALE");
        genderNotSpecifiedToggle.setUserData("NOT_SPECIFIED");

        setSkillUserData(skillAmateurToggle, skillBeginnerToggle, skillIntermediateToggle,
                skillExpertToggle, skillMasterToggle);

        instrumentRows.add(new InstrumentRow(instrumentNameField, skillToggleGroup));

        firstNameField.requestFocus();
    }

    private void setSkillUserData(ToggleButton amateur, ToggleButton beginner, ToggleButton intermediate,
                                   ToggleButton expert, ToggleButton master) {
        amateur.setUserData("AMATEUR");
        beginner.setUserData("BEGINNER");
        intermediate.setUserData("INTERMEDIATE");
        expert.setUserData("EXPERIENCED");
        master.setUserData("MASTER");
    }

    @FXML
    private void handleBackToLogin() {
        navigator.goToLogin();
    }

    @FXML
    private void handleAddInstrument() {
        TextField nameField = new TextField();
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPromptText("Instrument name (e.g. Bass)");
        nameField.getStyleClass().add("text-field");

        ToggleGroup skillGroup = new ToggleGroup();

        ToggleButton amateur = new ToggleButton("AMATEUR");
        ToggleButton beginner = new ToggleButton("BEGINNER");
        ToggleButton intermediate = new ToggleButton("INTERMEDIATE");
        ToggleButton expert = new ToggleButton("EXPERT");
        ToggleButton master = new ToggleButton("MASTER");

        setSkillUserData(amateur, beginner, intermediate, expert, master);

        HBox skillRow = new HBox(10.0, amateur, beginner, intermediate, expert, master);
        for (ToggleButton skillToggle : List.of(amateur, beginner, intermediate, expert, master)) {
            skillToggle.setToggleGroup(skillGroup);
            skillToggle.setMaxWidth(Double.MAX_VALUE);
            skillToggle.getStyleClass().add("skill-toggle");
            HBox.setHgrow(skillToggle, javafx.scene.layout.Priority.ALWAYS);
        }
        skillGroup.selectToggle(beginner);

        VBox instrumentRow = new VBox(10.0, nameField, skillRow);

        instrumentsContainer.getChildren().add(instrumentRow);
        instrumentRows.add(new InstrumentRow(nameField, skillGroup));
    }

    @FXML
    private void handleCreateAccount() {
        this.name = firstNameField.getText();
        this.surname = lastNameField.getText();
        this.stageName = stageNameField.getText();
        this.email = emailField.getText();
        this.password = passwordField.getText();

        Toggle selectedGender = genderToggleGroup.getSelectedToggle();
        if (selectedGender == null) {
            navigator.showError("Please select your gender.");
            return;
        }
        this.gender = (String) selectedGender.getUserData();

        List<InstrumentBean> collectedInstruments = new ArrayList<>();
        for (InstrumentRow row : instrumentRows) {
            String instrumentName = row.nameField().getText();
            if (instrumentName == null || instrumentName.isBlank()) {
                continue;
            }

            Toggle selectedSkill = row.skillGroup().getSelectedToggle();
            if (selectedSkill == null) {
                navigator.showError("Please select a skill level for \"" + instrumentName.trim() + "\".");
                return;
            }

            collectedInstruments.add(new InstrumentBean(instrumentName, (String) selectedSkill.getUserData()));
        }

        if (collectedInstruments.isEmpty()) {
            navigator.showError("Please add at least one instrument.");
            return;
        }

        this.instruments = collectedInstruments;

        doRegistration();
    }

}