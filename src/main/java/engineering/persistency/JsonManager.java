package engineering.persistency;

import exception.DAOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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

    //matcher is the condition to match: if the json object matches, it gets substituted.
    //if no object matches, the new object is added at the end
    //example: we want have a musician in memory to move in persistency: if the musician exists, we update it,
    //if not, we add it
    public static void upsert(JSONArray array, Predicate<JSONObject> matcher, JSONObject newObj) {
        for (int i = 0; i < array.length(); i++) {
            if (matcher.test(array.getJSONObject(i))) {
                array.put(i, newObj);
                return;
            }
        }
        array.put(newObj);
    }

    //this does the same thing as upsert, but also updates the file
    public static void upsertFile(String path, Predicate<JSONObject> matcher, JSONObject newObj) {
        JSONArray array = readJsonFile(path);
        upsert(array, matcher, newObj);
        writeJsonFile(array, path);
    }

    //returns the first object matching matcher, or null if none matches
    public static JSONObject findFirst(JSONArray array, Predicate<JSONObject> matcher) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (matcher.test(obj)) {
                return obj;
            }
        }
        return null;
    }

    public static JSONObject findInFile(String path, Predicate<JSONObject> matcher) {
        return findFirst(readJsonFile(path), matcher);
    }

    //returns every object matching matcher
    public static List<JSONObject> filter(JSONArray array, Predicate<JSONObject> matcher) {
        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (matcher.test(obj)) {
                result.add(obj);
            }
        }
        return result;
    }

    public static List<JSONObject> filterInFile(String path, Predicate<JSONObject> matcher) {
        return filter(readJsonFile(path), matcher);
    }

}
