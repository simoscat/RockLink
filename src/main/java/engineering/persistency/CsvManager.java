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

    /**
     * Replaces the first row whose fields match {@code matcher} with {@code newRow},
     * or appends {@code newRow} if no row matches, then rewrites the whole file.
     */
    public static void upsertRow(String path, Predicate<String[]> matcher, String newRow) {
        File file = new File(path);
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(SEPARATOR, -1);
                if (matcher.test(fields)) {
                    lines.add(newRow);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + path, e);
        }

        if (!found) {
            lines.add(newRow);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't write csv file " + path, e);
        }
    }

}
