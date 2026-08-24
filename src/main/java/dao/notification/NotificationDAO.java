package dao.notification;

import dao.DAOWithCache;
import model.Notification;

import java.util.List;

public abstract class NotificationDAO extends DAOWithCache<Notification> {

    public List<Notification> getUserNotificationsByEmail(String email){

        List<Notification> notifications = retrieveUserNotifications(email);

        for(Notification n : notifications){

            if (!isCached(n)){
                addToCache(n);
            }

        }

        return notifications;

    }


    @Override
    public String getKey(Notification obj) {
        return obj.getSender().getEmail() + "_" + obj.getTimeStamp().toString();
    }

    protected abstract List<Notification> retrieveUserNotifications(String email);

}
