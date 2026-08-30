package app;

import view.FontManager;
import javafx.application.Platform;
import view.Navigator;
import view.NavigatorFactory;

public class Main {
    static void main() {
        Platform.startup(() -> {
            FontManager.loadEmbeddedFonts();
            Navigator navigator = NavigatorFactory.getInstance().getNavigator();
            navigator.startUp();
        });
    }
}
