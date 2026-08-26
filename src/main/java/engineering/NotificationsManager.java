package engineering;

import bean.NotificationBean;
import bean.UserBean;
import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.notification.NotificationDAO;
import engineering.enums.Event;
import exception.DAOException;
import exception.NotificationException;
import model.JobAnnouncement;
import model.JobApplication;
import model.Notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class NotificationsManager {

    public List<NotificationBean> getNotifications(UserBean u){

        return getUserNotifications(u.getEmail());

    }

    private List<NotificationBean> getUserNotifications(String email){

        try{

            NotificationDAO notificationDAO = DAOFactory.getInstance().getNotificationDAO();

            List<Notification> notifications = notificationDAO.getUserNotificationsByEmail(email);

            List<NotificationBean> notificationBeans = new ArrayList<>();

            for (Notification notification : notifications){

                notificationBeans.add(BeanConverter.fromNotificationToBean(notification));

            }

            return notificationBeans;

        }
        catch(DAOException _){
            throw new NotificationException("Couldn't retrieve user notifications");
        }

    }

    public void notifyMusician(JobApplication jobApplication, Event event) {


        Notification n = new Notification(
                jobApplication.whichJobAnnouncement().getPublisher(),
                DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(jobApplication.whoIsCandidate().getEmail()),
                event,
                LocalDateTime.now(ZoneId.systemDefault()),
                jobApplication.whichJobAnnouncement()
        );

        DAOFactory.getInstance().getNotificationDAO().save(n);

        //note: the sender will always be the musician or the band leader, so we retrieve the specific user from the DAO

    }

    public void notifyPromoter(JobAnnouncement jobAnnouncement, JobApplication application, Event event) {

        Notification n = new Notification(
                DAOFactory.getInstance().getMusicianDAO().getMusicianByEmail(application.whoIsCandidate().getEmail()),
                jobAnnouncement.getPublisher(),
                event,
                LocalDateTime.now(ZoneId.systemDefault()),
                jobAnnouncement
        );

        DAOFactory.getInstance().getNotificationDAO().save(n);

    }

    public void notifyMusicians(JobAnnouncement jobAnnouncement) {

        try {
            MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();
            NotificationDAO notificationDAO = DAOFactory.getInstance().getNotificationDAO();

            List<String> emails = DAOFactory.getInstance().getMusicianDAO().getAllMusicianEmails();
            //right now, this sends the notification to all musicians, but it could be modified to
            // send to specific musicians based on their interests or location

            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

            for (String email : emails) {

                Notification n =  new Notification(
                        jobAnnouncement.getPublisher(),
                        musicianDAO.getMusicianByEmail(email),
                        Event.NEW_JOB_ANNOUNCEMENT,
                        now,
                        jobAnnouncement
                );

                notificationDAO.save(n);

            }
        } catch (DAOException _) {
            throw new NotificationException("Couldn't send notifications to musicians");
        }

    }

}
