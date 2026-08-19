package view.factories;


import view.Navigator;
import view.NavigatorGUI;

public class NavigatorFactoryGUI extends NavigatorFactory {

    @Override
    public Navigator getNavigator() {
        return new NavigatorGUI();
    }
}
