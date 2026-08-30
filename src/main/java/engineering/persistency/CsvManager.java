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

    /*
    Percent-encoding dei caratteri "strutturali" usati dai DAO CSV come separatori:
    virgola (separatore di campo), punto e virgola e due punti (separatori di lista
    e di coppia chiave/valore), più i ritorni a capo, dato che ogni record CSV
    occupa una singola riga.

    Il carattere '%' viene codificato per primo e decodificato per ultimo: è
    l'invariante che rende la trasformazione sempre reversibile, anche quando il
    valore originale contiene già sequenze tipo "%2C".
     */

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("%", "%25")
                .replace(",", "%2C")
                .replace(";", "%3B")
                .replace(":", "%3A")
                .replace("\r", "%0D")
                .replace("\n", "%0A");
    }

    public static String unescape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("%0A", "\n")
                .replace("%0D", "\r")
                .replace("%3A", ":")
                .replace("%3B", ";")
                .replace("%2C", ",")
                .replace("%25", "%");
    }

}
