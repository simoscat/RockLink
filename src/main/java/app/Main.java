package app;

import javafx.application.Platform;
import view.Navigator;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Navigator navigator = NavigatorFactory.getNavigatorFactory().createNavigator();
            navigator.startUp();
        });
    }
}
