package view;

import javafx.scene.Parent;

public interface GUIGraphicController {

    public void setNavigatorGUI(NavigatorGUI navigatorGUI);
    public void setView(Parent view);
    public Parent getView();

}
