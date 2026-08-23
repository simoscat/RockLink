package engineering.persistency;

import exception.DAOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;

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

    /**
     * Replaces the first object in {@code array} matching {@code matcher} with {@code newObj},
     * or appends {@code newObj} if no object matches.
     */
    public static void upsert(JSONArray array, Predicate<JSONObject> matcher, JSONObject newObj) {
        for (int i = 0; i < array.length(); i++) {
            if (matcher.test(array.getJSONObject(i))) {
                array.put(i, newObj);
                return;
            }
        }
        array.put(newObj);
    }

    /**
     * Reads the JSON file, upserts {@code newObj} per {@link #upsert}, then writes it back.
     */
    public static void upsertFile(String path, Predicate<JSONObject> matcher, JSONObject newObj) {
        JSONArray array = readJsonFile(path);
        upsert(array, matcher, newObj);
        writeJsonFile(array, path);
    }

}
