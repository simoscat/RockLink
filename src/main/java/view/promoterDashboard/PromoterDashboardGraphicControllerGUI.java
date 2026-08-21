package view.promoterDashboard;

import javafx.scene.Parent;
import view.GUIGraphicController;
import view.Navigator;
import view.NavigatorGUI;

//TODO
public class PromoterDashboardGraphicControllerGUI extends PromoterDashboardGraphicController implements GUIGraphicController {

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

    public PromoterDashboardGraphicControllerGUI(NavigatorGUI navigator) {
        super(navigator);
        this.navigatorGUI = navigator;
    }

    @Override
    public void start() {
        //TODO: build/show JavaFX scene
    }

    @Override
    public void showError(String message) {
        //TODO
    }

    @Override
    public void showInfo(String message) {
        //TODO
    }

}