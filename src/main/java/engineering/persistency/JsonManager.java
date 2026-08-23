package engineering.persistency;

import exception.DAOException;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public final class JsonManager {

    private JsonManager(){}

    public static JSONArray readJsonFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new JSONArray();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isBlank()) return new JSONArray();
            return new JSONArray(content);
        } catch (IOException e) {
            throw new DAOException("Couldn't read Json file " + path, e);
        }
    }

    public static void writeJsonFile(JSONArray array, String path) {

        File file = new File(path);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new DAOException("Unable to create directory " + parentDir.getAbsolutePath());
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            throw new DAOException("Couldn't write Json file " + path, e);
        }
    }

}
