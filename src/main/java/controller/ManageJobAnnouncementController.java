package controller;

import bean.JobAnnouncementBean;
import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.JobAnnouncementTag;
import engineering.persistency.JobDecoratorManager;
import exception.ControllerLogicException;
import exception.DAOException;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ManageJobAnnouncementController {

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


        } catch (DateTimeParseException e) {

            throw new ControllerLogicException("Invalid date format for the event: " + jobAnnouncementBean.getDate());

        } catch (DAOException e) {

            throw new ControllerLogicException("Could not publish job announcement");

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ControllerLogicException("Something went wrong. Please fill all the fields correctly", e);
        }

    }

    private void notifyMusicians(JobAnnouncement jobAnnouncement) {

        //TODO IMPLEMENTA NOTIFICHE ASINCRONE

    }

    private JobAnnouncement setDecorators(JobAnnouncement jobAnnouncement, List<JobAnnouncementTag> tags) {

        return JobDecoratorManager.applyDecorators(jobAnnouncement, tags);

    }

}
