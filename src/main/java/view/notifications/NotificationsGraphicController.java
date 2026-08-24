package view.notifications;

import bean.NotificationBean;
import engineering.NotificationsManager;
import exception.NotificationException;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class NotificationsGraphicController {

    protected static final String APPLICATION_FORMAT = "Your application for %s [Promoter %s] was %s";
    protected static final String NEW_JOB_FORMAT = "New job announcement by %s: %s";
    protected static final String NEW_APPLICATION_FORMAT = "%s applied for your job announcement: %s";

    protected static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

    protected Navigator navigator;
    protected NotificationsManager notificationsManager = new NotificationsManager();

    protected NotificationsGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected void backToDashboard(){

        if (navigator.getMusician() != null){
            navigator.goToMusicianDashboard();
        }
        else{
            navigator.goToPromoterDashboard();
        }

    }

    protected List<NotificationBean> getNotifications(){

        try {

            navigator.setNotifications(notificationsManager.getNotifications(
                    navigator.getMusician() != null ? navigator.getMusician() :
                            navigator.getPromoter()
            ));

            return navigator.getNotifications();
        }
        catch (NotificationException e){
            navigator.showError(e.getMessage());
        }
        catch (RuntimeException e){
            navigator.showError("Internal error: "+e.getMessage());
        }

        return new ArrayList<>();

    }

    protected void goToJobAnnouncement(){

        navigator.goToAnnouncementDetails();

    }

    protected void refreshUI(){
        start();
    }

    protected String buildBody(NotificationBean notification) {

        String title = notification.getJobAnnouncement().getTitle();

        return switch (notification.getEvent()) {

            case NEW_JOB_ANNOUNCEMENT -> String.format(NEW_JOB_FORMAT, notification.getSender(), title);

            case APPLICATION_ACCEPTED -> String.format(APPLICATION_FORMAT, title, notification.getSender(),
                    "accepted");

            case APPLICATION_REJECTED -> String.format(APPLICATION_FORMAT, title, notification.getSender(),
                    "rejected");

            case NEW_APPLICATION -> String.format(NEW_APPLICATION_FORMAT, notification.getSender(), title);

        };

    }

    public abstract void start();

}
