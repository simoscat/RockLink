package controller;

import bean.*;
import dao.announcement.JobAnnouncementDAO;
import dao.application.JobApplicationDAO;
import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import exception.ControllerLogicException;
import exception.DAOException;
import model.JobAnnouncement;
import model.JobApplication;
import model.MoneyValue;
import model.Promoter;

import java.util.ArrayList;
import java.util.List;

//TODO
public class ManageJobApplicationsController {

    private final MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();
    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();
    private final JobApplicationDAO jobApplicationDAO = DAOFactory.getInstance().getJobApplicationDAO();

    public List<JobAnnouncementBean> findOpenJobAnnouncements() {

        try {

            return fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllOpenJobAnnouncements());


        }
        catch (DAOException e) {

            throw new ControllerLogicException("No open announcements found");

        }

    }

    public List<JobAnnouncementBean> findAllJobAnnouncements() {

        try{

            return fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllJobAnnouncements());

        }
        catch (DAOException e) {
            throw new ControllerLogicException("No announcements found");
        }
    }

    private List<JobAnnouncementBean> fromJobAnnouncementsToBeans(List<JobAnnouncement> jobAnnouncements) {

        List<JobAnnouncementBean> beans = new ArrayList<>();

        for (JobAnnouncement jobAnnouncement : jobAnnouncements) {

            JobAnnouncementBean jAB = fromJobAnnouncementToBean(jobAnnouncement);

            beans.add(jAB);

        }

        return beans;

    }

    public List<JobApplicationBean> findMusicianJobApplications(MusicianBean m){

        try {

            List<JobApplication> jobApplications = jobApplicationDAO.getAllMusicianJobApplicationsFromEmail(m.getEmail());
            List<JobApplicationBean> beans = new ArrayList<>();

            for (JobApplication jobApplication : jobApplications) {

                beans.add(fromJobApplicationToBean(jobApplication));

            }
            return beans;

        }
        catch (DAOException e) {
            throw new ControllerLogicException("No job applications found for this musician");
        }
    }

    public List<JobAnnouncementBean> findPromoterPublishedJobAnnouncements(PromoterBean p){

        try{

            List<JobAnnouncement> jobs = jobAnnouncementDAO.getAllPromoterAnnouncementsFromEmail(p.getEmail());

            List<JobAnnouncementBean> beans = new ArrayList<>();

            for (JobAnnouncement job : jobs){

                beans.add(fromJobAnnouncementToBean(job));

            }

            return beans;

        }
        catch (DAOException e) {

            throw new ControllerLogicException("No job announcements found for this promoter");

        }

    }

    public List<JobApplicationBean> findJobAnnouncementApplications(JobAnnouncementBean jobAnnouncement){
        //TODO
    }

    //TODO


    // bean conversion utilities

    private PromoterBean fromPromoterToBean(Promoter p){

        return new PromoterBean(
                p.getName(),
                p.getSurname(),
                p.getEmail(),
                p.getGender().name(),
                "", //password is only used in login
                p.promoterContacts()
        );

    }

    private JobAnnouncementBean fromJobAnnouncementToBean(JobAnnouncement ja){

        return new JobAnnouncementBean(
                fromPromoterToBean(ja.getPublisher()),
                fromMoneyValueToBean(ja.getJobPay()),
                ja.getEventAddress(),
                ja.whoWasHired(),
                ja.getStatus().name()
        );

    }

    private MoneyValueBean fromMoneyValueToBean(MoneyValue mv){

        return new MoneyValueBean(
                mv.whichCurrency().name(),
                mv.moneyAmount()
        );

    }

    private JobApplicationBean fromJobApplicationToBean(JobApplication ja){

        return new JobApplicationBean(
                ja.currentRaiseAmount(),
                fromJobAnnouncementToBean(ja.whichJobAnnouncement())
        );

    }

}