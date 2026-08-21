package view.createAnnouncement;

import javafx.scene.Parent;
import view.GUIGraphicController;
import view.Navigator;
import view.NavigatorGUI;

//TODO
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
        //TODO: build/show JavaFX scene
    }

    @Override
    protected void showError(String message) {
        //TODO
    }

    @Override
    protected void showInfo(String message) {
        //TODO
    }

}