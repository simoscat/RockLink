package dao.notification;

import dao.factories.DAOFactory;
import engineering.enums.Event;
import engineering.persistency.ConfigManager;
import engineering.persistency.JsonManager;
import model.Notification;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOJson extends NotificationDAO {

    private final String path;
    private static final String RECEIVER_FIELD = "receiver";
    private static final String SENDER_FIELD = "sender";
    private static final String ANNOUNCEMENT_ID_FIELD = "announcementId";


    public NotificationDAOJson() {

        path = ConfigManager.getProperty("json.path") + "notifications.json";

    }

    @Override
    protected List<Notification> retrieveUserNotifications(String email) {

        JSONArray array = JsonManager.readJsonFile(path);

        List<Notification> notifications = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {

            JSONObject obj = array.getJSONObject(i);

            if (obj.getString(RECEIVER_FIELD).equals(email)) {
                notifications.add(parseJson(obj));
            }

        }

        return notifications;

    }

    @Override
    protected void saveToPersistency(Notification obj) {

        JSONObject jsonObj = toJson(obj);

        JSONArray array = JsonManager.readJsonFile(path);

        array.put(jsonObj);

        JsonManager.writeJsonFile(array, path);

    }

    private JSONObject toJson(Notification obj) {

        JSONObject json = new JSONObject();

        json.put(SENDER_FIELD, obj.getSender().getEmail());
        json.put(RECEIVER_FIELD, obj.getReceiver().getEmail());
        json.put("timeStamp", obj.getTimeStamp().toString());
        json.put("event", obj.getEvent().name());
        json.put(ANNOUNCEMENT_ID_FIELD,
                DAOFactory.getInstance().getJobAnnouncementDAO().getUniqueId(obj.getJobAnnouncement()));

        return json;
    }

    private Notification parseJson(JSONObject obj) {

        return new Notification(
            getUserByEmail(obj.getString(SENDER_FIELD)),
                getUserByEmail(obj.getString(RECEIVER_FIELD)),
                Event.valueOf(obj.getString("event")),
                LocalDateTime.parse(obj.getString("timeStamp")),
                DAOFactory.getInstance().getJobAnnouncementDAO()
                        .getAnnouncementFromId(obj.getString(ANNOUNCEMENT_ID_FIELD))
        );

    }

    private User getUserByEmail(String email) {

        if (DAOFactory.getInstance().getAuthDAO().isMusicianAlreadyRegistered(email)){
            return DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(email);
        }
        else{
            return DAOFactory.getInstance().getPromoterDAO().getPromoterByEmail(email);
        }

    }
}
