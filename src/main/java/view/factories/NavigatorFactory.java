package view.factories;

import engineering.persistency.ConfigManager;
import engineering.enums.SupportedUI;
import exception.DAOException;
import view.Navigator;

public abstract class NavigatorFactory {

    private static NavigatorFactory instance;

    public static NavigatorFactory getInstance() {

        if (instance == null) {

            String uiType;

            try {
                uiType = ConfigManager.getProperty("ui.type", "CLI");
            } catch (DAOException _) {
                System.err.println("NavigatorFactory could not open config.properties, falling back to default values (CLI)");
                uiType = "CLI";
            }

            if (SupportedUI.valueOf(uiType).equals(SupportedUI.GUI)) {
                instance = new NavigatorFactoryGUI();
            }
            else{
                instance = new NavigatorFactoryCLI();
            }
        }

        return instance;
    }

    public abstract Navigator getNavigator();

}
