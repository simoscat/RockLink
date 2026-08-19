package app;

import javafx.application.Platform;
import view.Navigator;
import view.factories.NavigatorFactory;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Navigator navigator = NavigatorFactory.getInstance().getNavigator();
            navigator.startUp();
        });
    }
}
