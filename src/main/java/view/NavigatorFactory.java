package view;

import engineering.persistency.ConfigManager;
import engineering.enums.SupportedUI;
import exception.DAOException;

public class NavigatorFactory {

    private static NavigatorFactory instance;

    private NavigatorFactory() {}

    public static synchronized NavigatorFactory getInstance() {

        if (instance == null) {
            instance = new NavigatorFactory();
        }

        return instance;
    }

    public Navigator getNavigator(){
        String uiType;

        try {
            uiType = ConfigManager.getProperty("ui.type", "CLI");
        } catch (DAOException _) {
            System.err.println("NavigatorFactory could not open config.properties, falling back to default values (CLI)");
            uiType = "CLI";
        } catch (IllegalArgumentException _){
            System.err.println("NavigatorFactory received an invalid UI type, falling back to default values (CLI)");
            uiType = "CLI";
        }

        if (SupportedUI.valueOf(uiType).equals(SupportedUI.GUI)) {
            return new NavigatorGUI();
        }
        else{
            return new NavigatorCLI();
        }
    }

}
