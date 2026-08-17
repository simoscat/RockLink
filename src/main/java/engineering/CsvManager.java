package engineering;

import exception.DAOException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CsvManager {

    public static void initCsvFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                Files.createDirectories(parentDir.toPath());
            }

            file.createNewFile(); //creates the csv if it doesn't exist
        }
    }

}