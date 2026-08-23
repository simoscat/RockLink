package dao.promoter;

import engineering.persistency.ConfigManager;
import engineering.persistency.CsvManager;
import engineering.enums.Gender;
import exception.DAOException;
import model.Promoter;

import java.io.IOException;
import java.util.*;

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
        path = ConfigManager.getProperty("csv.path") + "promoters.csv";

        try {
            CsvManager.initCsvFile(path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + path, e);
        }
    }

    @Override
    protected Promoter retrievePromoterByEmail(String email) {
        String[] fields = CsvManager.findRow(this.path, f -> f.length >= MIN_FIELDS && f[0].equals(email));

        if (fields == null) {
            throw new DAOException("No promoter found with email: " + email);
        }

        return parseRow(fields);
    }

    @Override
    protected void saveToPersistency(Promoter promoter) {
        CsvManager.upsertRow(this.path, fields -> fields[0].equals(promoter.getEmail()), toCsvRow(promoter));
    }

    private Promoter parseRow(String[] fields) {
        try {
            String csvEmail = fields[0];
            String name = fields[1];
            String surname = fields[2];
            Gender gender = Gender.valueOf(fields[3]);

            Map<String, String> contacts = parseContacts(fields.length > MIN_FIELDS ? fields[MIN_FIELDS] : "");

            return new Promoter(name, surname, csvEmail, gender, contacts);

        } catch (IllegalArgumentException e) {
            throw new DAOException("Invalid csv line for email " + fields[0], e);
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

            map.put(key.replace("%2C", ","), value.replace("%2C", ","));
        }

        return map;
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
            sb.append(e.getKey().replace(",", "%2C"))
                    .append(CONTACT_PAIR_SEPARATOR).append(e.getValue().replace(",", "%2C"));
            first = false;
        }
        return sb.toString();
    }
}

