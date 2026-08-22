package dao.promoter;

import engineering.persistency.CsvManager;
import engineering.enums.Gender;
import exception.DAOException;
import model.Promoter;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.Properties;

public class PromoterDAOCsv extends PromoterDAO {

    /*
    Structure:
    email,name,surname,gender,contactname1:contact1;contactname2:contact2;...;contactnameN:contactN
     */

    private final String path;
    private static final String CSV_SEPARATOR = ",";
    private static final String CONTACT_PAIR_SEPARATOR = ":"; // key:value
    private static final String CONTACTS_SEPARATOR = ";";
    private static final int MIN_FIELDS = 4;

    public PromoterDAOCsv() {
        try(InputStream is = new FileInputStream("config.properties")){

            Properties prop = new Properties();
            prop.load(is);
            path = prop.getProperty("csv.path") + "promoters.csv";

        } catch (FileNotFoundException e) {

            throw new DAOException("Couldn't find properties file", e);

        } catch (IOException e) {

            throw new DAOException("Couldn't read properties file", e);

        }

        try {
            CsvManager.initCsvFile(path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + path, e);
        }
    }

    @Override
    protected Promoter retrievePromoterByEmail(String email) {
        File file = new File(this.path);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) continue;

                Promoter candidate = parseRowIfMatches(line, email);

                if (candidate != null){
                    return candidate;
                }

            }

            throw new DAOException("No promoter found with email: " + email);

        } catch (IOException e) {
            throw new DAOException("Couldn't read promoter file: " + path, e);
        }
    }

    @Override
    protected void saveToPersistency(Promoter promoter) {
        List<String> lines = readAllLinesReplacingPromoter(promoter);

        File file = new File(this.path);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't write to file " + this.path, e);
        }
    }

    private Promoter parseRowIfMatches(String line, String email) {
        try {
            String[] fields = line.split(CSV_SEPARATOR, -1);

            if (fields.length < MIN_FIELDS || !fields[0].equals(email)) return null;

            String csvEmail = fields[0];
            String name = fields[1];
            String surname = fields[2];
            Gender gender = Gender.valueOf(fields[3]);

            Map<String, String> contacts = parseContacts(fields.length > MIN_FIELDS ? fields[MIN_FIELDS] : "");

            return new Promoter(name, surname, csvEmail, gender, contacts);

        } catch (IllegalArgumentException e) {
            throw new DAOException("Invalid csv line: " + line, e);
        }
    }

    private Map<String, String> parseContacts(String csv) {
        Map<String, String> map = new HashMap<>();
        if (csv == null || csv.isBlank()){
            return map;
        }

        String[] pairs = csv.split(CONTACTS_SEPARATOR, -1);

        for (String pair : pairs) {

            if (pair.isBlank()){
                continue;
            }

            String[] kv = pair.split(CONTACT_PAIR_SEPARATOR, 2);

            String key = kv.length > 0 ? kv[0] : "";

            String value = kv.length > 1 ? kv[1] : "";

            map.put(key, value);
        }

        return map;
    }

    private List<String> readAllLinesReplacingPromoter(Promoter p) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(this.path);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields.length > 0 && fields[0].equals(p.getEmail())) {
                    lines.add(toCsvRow(p));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + this.path, e);
        }

        if (!found) lines.add(toCsvRow(p));

        return lines;
    }

    private String toCsvRow(Promoter p) {
        String contactsCsv = contactsToCsv(p.promoterContacts());
        return String.join(CSV_SEPARATOR,
                p.getEmail(),
                p.getName(),
                p.getSurname(),
                p.getGender().name(),
                contactsCsv);
    }

    private String contactsToCsv(Map<String, String> contacts) {
        if (contacts == null || contacts.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> e : contacts.entrySet()) {
            if (!first) sb.append(CONTACTS_SEPARATOR);
            sb.append(e.getKey()).append(CONTACT_PAIR_SEPARATOR).append(e.getValue());
            first = false;
        }
        return sb.toString();
    }
}

