package dao.notification;

import model.Notification;

import java.util.List;

public abstract class NotificationDAO {

    public abstract void save(Notification n);

    public abstract List<Notification> getUserNotificationsByEmail(String email);

}
