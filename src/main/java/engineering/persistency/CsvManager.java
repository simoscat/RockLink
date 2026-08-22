package engineering.persistency;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class CsvManager {

    private CsvManager() {}

    public static void initCsvFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                Files.createDirectories(parentDir.toPath());
            }

            if (!file.createNewFile()){
                throw new IOException("Failed to create CSV File: " + path );//creates the csv if it doesn't exist
            }
        }
    }

}
