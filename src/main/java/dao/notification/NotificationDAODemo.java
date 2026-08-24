package dao.notification;

import dao.factories.DAOFactory;
import engineering.enums.Event;
import model.JobAnnouncement;
import model.Notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAODemo extends NotificationDAO {

    private List<Notification> notifications;

    public NotificationDAODemo() {

        JobAnnouncement jobAnnouncement = DAOFactory.getInstance().getJobAnnouncementDAO()
                .getAllPromoterAnnouncementsFromEmail("marco.santodonato@libero.it").get(0);

        Notification n = new Notification(
                DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail("anna.muscatello@gmail.com"),
                DAOFactory.getInstance().getPromoterDAO().getPromoterByEmail("marco.santodonato@libero.it"),
                Event.NEW_APPLICATION,
                LocalDateTime.now(ZoneId.systemDefault()),
                jobAnnouncement
        );

        notifications = new ArrayList<>();
        notifications.add(n);

    }

    @Override
    protected List<Notification> retrieveUserNotifications(String email) {

        List<Notification> toRet = new ArrayList<>();

        for (Notification notification : notifications) {

            if (notification.getReceiver().getEmail().equals(email)) {
                toRet.add(notification);
            }

        }

        return toRet;

    }

    @Override
    protected void saveToPersistency(Notification obj) {

        notifications.add(obj);

    }
}
