package view.factories;

import java.io.BufferedReader;
import java.nio.file.Files;

public abstract class NavigatorFactory {

    private static NavigatorFactory instance;

    private NavigatorFactory() {

        try(BufferedReader reader = Files.newBufferedReader("config.properties")){



        }

    }

    public static NavigatorFactory getInstance() {
        if (instance == null) {

        }
    }

}
