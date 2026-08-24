package controller;

import bean.JobAnnouncementBean;
import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.notification.NotificationDAO;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.Event;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.JobAnnouncementTag;
import engineering.persistency.JobDecoratorManager;
import exception.ControllerLogicException;
import exception.DAOException;
import model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PublishJobAnnouncementController {

    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

    public void publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {

        try {
            Promoter promoter = promoterDAO.getPromoterByEmail(jobAnnouncementBean.getPromoter().getEmail());

            LocalDateTime eventDate;


            eventDate = jobAnnouncementBean.getDate();

            CurrencyType currency;

            currency = CurrencyType.valueOf(jobAnnouncementBean.getMoneyValue().getCurrency().toUpperCase());


            MoneyValue salary = new MoneyValue(jobAnnouncementBean.getMoneyValue().getValue(), currency);

            JobAnnouncement jobAnnouncement = new ConcreteJobAnnouncement(
                    jobAnnouncementBean.getTitle(),
                    jobAnnouncementBean.getContent(),
                    eventDate,
                    JobAnnouncementStatus.OPEN,
                    promoter,
                    salary,
                    jobAnnouncementBean.getAddress()
            );

            jobAnnouncement = setDecorators(jobAnnouncement, jobAnnouncementBean.getTags());

            jobAnnouncement.publishNow();

            jobAnnouncementDAO.save(jobAnnouncement);

            notifyMusicians(jobAnnouncement);


        } catch (DateTimeParseException _) {

            throw new ControllerLogicException("Invalid date format for the event: " + jobAnnouncementBean.getDate());

        } catch (DAOException _) {

            throw new ControllerLogicException("Could not publish job announcement");

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ControllerLogicException("Something went wrong. Please fill all the fields correctly", e);
        }

    }

    private void notifyMusicians(JobAnnouncement jobAnnouncement) {

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
            throw new ControllerLogicException("Couldn't send notifications to musicians");
        }

    }

    private JobAnnouncement setDecorators(JobAnnouncement jobAnnouncement, List<JobAnnouncementTag> tags) {

        return JobDecoratorManager.applyDecorators(jobAnnouncement, tags);

    }

}
