package dao.notification;

import dao.factories.DAOFactory;
import engineering.enums.Event;

import engineering.persistency.ConfigManager;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Notification;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOCsv extends NotificationDAO {

    private final String path;
    private static final String CSV_SEPARATOR = ",";
    private static final int RECEIVER_FIELD = 1;
    private static final int ANNOUNCEMENT_ID_FIELD = 4;

    public NotificationDAOCsv() {
        path = ConfigManager.getProperty("csv.path") + "notifications.csv";

        try {
            CsvManager.initCsvFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.path, e);
        }
    }

    @Override
    public List<Notification> getUserNotificationsByEmail(String email) {

        File file = new File(path);
        List<Notification> notifications = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {

            String line;

            while ((line = br.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields[RECEIVER_FIELD].equals(email)){
                    notifications.add(parseFields(fields));
                }

            }

            return notifications;

        } catch (IOException e) {
            throw new DAOException("Couldn't retrieve user notifications", e);
        }

    }

    private Notification parseFields(String[] fields) {

        return new Notification(
                getUserByEmail(fields[0]),
                getUserByEmail(fields[RECEIVER_FIELD]),
                Event.valueOf(fields[2]),
                LocalDateTime.parse(fields[3]),
                DAOFactory.getInstance().getJobAnnouncementDAO().getAnnouncementFromId(fields[ANNOUNCEMENT_ID_FIELD])
        );

    }

    private User getUserByEmail(String email) {

        if (DAOFactory.getInstance().getAuthDAO().isMusicianAlreadyRegistered(email)){
            return DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(email);
        }

        return DAOFactory.getInstance().getPromoterDAO().getPromoterByEmail(email);

    }

    @Override
    public void save(Notification obj) {

        File file = new File(path);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(toCsv(obj));
            bw.newLine();
        }

        catch (IOException e) {
            throw new DAOException("Couldn't save notification", e);
        }

    }

    private String toCsv(Notification obj) {

        return String.join(
                CSV_SEPARATOR,
                obj.getSender().getEmail(),
                obj.getReceiver().getEmail(),
                obj.getEvent().name(),
                obj.getTimeStamp().toString(),
                DAOFactory.getInstance().getJobAnnouncementDAO().getUniqueId(obj.getJobAnnouncement())
        );

    }
}
