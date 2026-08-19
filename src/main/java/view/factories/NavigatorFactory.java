package view.factories;

import engineering.enums.SupportedUI;
import view.Navigator;

import java.io.*;
import java.lang.module.FindException;
import java.nio.file.Files;
import java.util.Properties;

public abstract class NavigatorFactory {

    private static NavigatorFactory instance;

    public static NavigatorFactory getInstance() {

        if (instance == null) {

            String uiType = "CLI";

            try(InputStream input = new FileInputStream("config.properties")){

                Properties prop = new Properties();
                prop.load(input);
                uiType = prop.getProperty("ui.type");

            } catch (IOException _) {
                System.err.println("NavigatorFactory could not open config.properties, falling back to default values (CLI)");
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
