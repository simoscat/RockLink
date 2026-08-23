package engineering.persistency;

import exception.DAOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class CsvManager {

    private static final String SEPARATOR = ",";

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

    //matcher is the condition to match: if the row matches, it gets substituted.
    //if no row matches, the new row is added at the end
    //example: we want have a musician in memory to move in persistency: if the musician exists, we update it,
    //if not, we add it
    public static void upsertRow(String path, Predicate<String[]> matcher, String newRow) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        for (String line : readAllLines(path)) {
            if (matcher.test(line.split(SEPARATOR, -1))) {
                lines.add(newRow);
                found = true;
            } else {
                lines.add(line);
            }
        }

        if (!found) {
            lines.add(newRow);
        }

        writeAllLines(path, lines);
    }

    //reads every non-blank line of the file
    public static List<String> readAllLines(String path) {
        File file = new File(path);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + path, e);
        }

        return lines;
    }

    public static void writeAllLines(String path, List<String> lines) {
        File file = new File(path);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't write csv file " + path, e);
        }
    }

    //returns the fields of the first row matching matcher, or null if none matches
    public static String[] findRow(String path, Predicate<String[]> matcher) {
        for (String line : readAllLines(path)) {
            String[] fields = line.split(SEPARATOR, -1);
            if (matcher.test(fields)) {
                return fields;
            }
        }
        return null;
    }

    //returns the fields of every row matching matcher
    public static List<String[]> filterRows(String path, Predicate<String[]> matcher) {
        List<String[]> result = new ArrayList<>();
        for (String line : readAllLines(path)) {
            String[] fields = line.split(SEPARATOR, -1);
            if (matcher.test(fields)) {
                result.add(fields);
            }
        }
        return result;
    }

}
