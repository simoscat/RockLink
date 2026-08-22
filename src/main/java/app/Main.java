package app;

import javafx.application.Platform;
import view.Navigator;
import view.factories.NavigatorFactory;

public class Main {
    static void main() {
        Platform.startup(() -> {
            Navigator navigator = NavigatorFactory.getInstance().getNavigator();
            navigator.startUp();
        });
    }
}
