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
import java.util.ArrayList;
import java.util.List;

public class PublishJobPostingController {

    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

    public void publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {

        try {
            Promoter promoter = promoterDAO.getPromoterByEmail(jobAnnouncementBean.getPromoter().getEmail());

            LocalDateTime eventDate;


            eventDate = LocalDateTime.parse(jobAnnouncementBean.getDate());

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

            jobAnnouncement = convertAndSetDecorators(jobAnnouncement, jobAnnouncementBean.getTags());

            jobAnnouncement.publishNow();

            jobAnnouncementDAO.save(jobAnnouncement);


        } catch (DateTimeParseException e) {

            throw new ControllerLogicException("Invalid date format for the event: " + jobAnnouncementBean.getDate());

        } catch (DAOException e) {

            throw new ControllerLogicException("Could not publish job announcement");

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ControllerLogicException("Something went wrong. Please fill all the fields correctly");
        }

    }

    private JobAnnouncement convertAndSetDecorators(JobAnnouncement jobAnnouncement, List<String> tags) {

        List<JobAnnouncementTag> actualTags = new ArrayList<>();

        for (String tag : tags) {

            if (tag.equals(JobAnnouncementTag.URGENT.name())){
                actualTags.add(JobAnnouncementTag.URGENT);
            }
            else if (tag.equals(JobAnnouncementTag.EXPERTS_ONLY.name())){
                actualTags.add(JobAnnouncementTag.EXPERTS_ONLY);
            }
            else if (tag.equals(JobAnnouncementTag.LONG_TIME_CONTRACT.name())){
                actualTags.add(JobAnnouncementTag.LONG_TIME_CONTRACT);
            }
            else if (tag.equals(JobAnnouncementTag.NEGOTIABLE_SALARY.name())){
                actualTags.add(JobAnnouncementTag.NEGOTIABLE_SALARY);
            }

        }

        return JobDecoratorManager.applyDecorators(jobAnnouncement, actualTags);

    }

}
