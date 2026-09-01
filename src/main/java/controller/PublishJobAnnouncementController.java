package controller;

import bean.JobAnnouncementBean;
import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.NotificationsManager;
import model.CurrencyType;
import model.JobAnnouncementStatus;
import model.JobAnnouncementTag;
import engineering.persistency.JobDecoratorManager;
import exception.ControllerLogicException;
import exception.DAOException;
import model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PublishJobAnnouncementController {

    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();
    private final NotificationsManager notificationsManager = new NotificationsManager();

    public void publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {

        try {
            Promoter promoter = promoterDAO.getPromoterByEmail(jobAnnouncementBean.getPromoter().getEmail());

            LocalDateTime eventDate;


            eventDate = jobAnnouncementBean.getDate();

            if (eventDate == null){
                throw new ControllerLogicException("Date can't be empty");
            }

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

            notificationsManager.notifyMusicians(jobAnnouncement);


        } catch (DateTimeParseException _) {

            throw new ControllerLogicException("Invalid date format for the event: " + jobAnnouncementBean.getDate());

        } catch (DAOException _) {

            throw new ControllerLogicException("Could not publish job announcement");

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ControllerLogicException("Something went wrong. Please fill all the fields correctly", e);
        }

    }

    private JobAnnouncement setDecorators(JobAnnouncement jobAnnouncement, List<JobAnnouncementTag> tags) {

        return JobDecoratorManager.applyDecorators(jobAnnouncement, tags);

    }

}
