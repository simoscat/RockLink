package view.openAnnouncementsDiscovery;

import javafx.scene.Parent;
import view.GUIGraphicController;
import view.Navigator;
import view.NavigatorGUI;

//TODO
public class OpenAnnouncementsDiscoveryGraphicControllerGUI extends OpenAnnouncementsDiscoveryGraphicController implements GUIGraphicController {
    private NavigatorGUI navigatorGUI;

    public void setNavigatorGUI(NavigatorGUI navigatorGUI) {
        this.navigatorGUI = navigatorGUI;
    }
    public OpenAnnouncementsDiscoveryGraphicControllerGUI(Navigator navigator) {
        super(navigator);
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