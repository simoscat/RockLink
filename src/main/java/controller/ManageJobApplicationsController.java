package controller;

import bean.*;
import dao.announcement.JobAnnouncementDAO;
import dao.application.JobApplicationDAO;
import dao.factories.DAOFactory;
import engineering.BeanConverter;
import engineering.enums.*;
import exception.ControllerLogicException;
import exception.DAOException;
import model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ManageJobApplicationsController {

    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();
    private final JobApplicationDAO jobApplicationDAO = DAOFactory.getInstance().getJobApplicationDAO();

    public List<JobAnnouncementBean> findOpenJobAnnouncements() {

        try {

            return BeanConverter.fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllOpenJobAnnouncements());


        }
        catch (DAOException _) {

            throw new ControllerLogicException("Could not retrieve open job announcements");

        }

    }

    public List<JobAnnouncementBean> findAllJobAnnouncements() {

        try{

            return BeanConverter.fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllJobAnnouncements());

        }
        catch (DAOException _) {
            throw new ControllerLogicException("Could not retrieve job announcements");
        }
    }

    public List<JobApplicationBean> findMusicianJobApplications(SessionBean session){

        try {

            MusicianBean m = session.getMusician();

            List<JobApplication> jobApplications = jobApplicationDAO.getAllMusicianJobApplicationsFromEmail(m.getEmail());
            List<JobApplicationBean> beans = new ArrayList<>();

            for (JobApplication jobApplication : jobApplications) {

                beans.add(BeanConverter.fromJobApplicationToBean(jobApplication));

            }
            return beans;

        }
        catch (DAOException _) {
            throw new ControllerLogicException("Could not retrieve job applications for this musician");
        }
    }

    public JobApplicationBean findMusicianJobApplication(MusicianBean musicianBean,
                                                         JobAnnouncementBean jobAnnouncement){

        return BeanConverter.fromJobApplicationToBean(
                jobApplicationDAO.getJobApplication(musicianBean.getEmail(),
                        BeanConverter.fromBeanToJobAnnouncement(jobAnnouncement))
        );

    }

    public List<JobAnnouncementBean> findPromoterPublishedJobAnnouncements(SessionBean session){

        try{
            PromoterBean p = session.getPromoter();

            List<JobAnnouncement> jobs = jobAnnouncementDAO.getAllPromoterAnnouncementsFromEmail(p.getEmail());

            List<JobAnnouncementBean> beans = new ArrayList<>();

            for (JobAnnouncement job : jobs){

                beans.add(BeanConverter.fromJobAnnouncementToBean(job));

            }

            return beans;

        }
        catch (DAOException _) {

            throw new ControllerLogicException("No job announcements found for this promoter");

        }

    }

    public List<JobApplicationBean> findJobAnnouncementApplications(JobAnnouncementBean jobAnnouncement){

        JobAnnouncement job = BeanConverter.fromBeanToJobAnnouncement(jobAnnouncement);

        List<JobApplication> applications = jobApplicationDAO.getAllJobAnnouncementApplications(job);

        List<JobApplicationBean> beans = new ArrayList<>();

        for (JobApplication jobApplication : applications){

            beans.add(BeanConverter.fromJobApplicationToBean(jobApplication));
        }

        return beans;
    }

    public void acceptApplication(JobApplicationBean jobApplicationBean){

        try {
            JobApplication jobApplication = BeanConverter.fromBeanToJobApplication(jobApplicationBean);

            JobAnnouncement jobAnnouncement = jobApplication.whichJobAnnouncement();

            if (jobAnnouncement.getStatus().equals(JobAnnouncementStatus.FILLED)){
                throw new ControllerLogicException("Another applicant was already chosen for this job");
            }

            else if (jobAnnouncement.getStatus().equals(JobAnnouncementStatus.CLOSED)){
                throw new ControllerLogicException("This job announcement is closed");
            }

            if (!jobApplication.currentApplicationStatus().equals(ApplicationStatus.PENDING)) {
                throw new ControllerLogicException("You already marked this application as "+
                        jobApplication.currentApplicationStatus());
            }

            jobApplication.acceptApplication();

            jobAnnouncement.hireArtist(jobApplication.whoIsCandidate());

            jobApplicationDAO.save(jobApplication);

            jobAnnouncementDAO.save(jobAnnouncement);

            List<JobApplication> toReject = jobApplicationDAO.getAllJobAnnouncementApplications(jobAnnouncement);

            for (JobApplication jobApplicationToReject : toReject){

                if (!jobApplicationToReject.whoIsCandidate().getEmail().
                        equals(jobApplication.whoIsCandidate().getEmail())){

                    jobApplicationToReject.rejectApplication();
                    jobApplicationDAO.save(jobApplicationToReject);

                }

            }

        } catch (DAOException _) {
            throw new ControllerLogicException("Could not accept job application");
        }

    }

    public void rejectApplication(JobApplicationBean jobApplicationBean){

        try {
            JobApplication jobApplication = BeanConverter.fromBeanToJobApplication(jobApplicationBean);

            if (jobApplication.currentApplicationStatus().equals(ApplicationStatus.PENDING)) {

                jobApplication.rejectApplication();
                jobApplicationDAO.save(jobApplication);

            }
            else throw new ControllerLogicException("This application is already "
                    +jobApplication.currentApplicationStatus().name());

        } catch (DAOException _) {
            throw new ControllerLogicException("Could not reject job application");
        }

    }

    public void closeJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        try {
            JobAnnouncement job = BeanConverter.fromBeanToJobAnnouncement(jobAnnouncementBean);

            if (job.whoWasHired() != null){
                throw new ControllerLogicException("Can't close job announcement because someone" +
                        "was already hired");
            }

            job.closeAnnouncement();

            jobAnnouncementDAO.save(job);

            List<JobApplication> applicationsToReject = jobApplicationDAO.getAllJobAnnouncementApplications(job);

            for (JobApplication jobApplication : applicationsToReject) {

                jobApplication.rejectApplication();
                jobApplicationDAO.save(jobApplication);

            }
        }
        catch (DAOException _) {
            throw new ControllerLogicException("Could not close job announcement");
        }

    }

    public void applyForJobAnnouncement(JobAnnouncementBean jobAnnouncementBean, MusicianBean applicant,
                                        BigDecimal raiseOffer){

        try {

            Musician m = BeanConverter.fromBeanToMusician(applicant);

            JobAnnouncement jobAnnouncement = BeanConverter.fromBeanToJobAnnouncement(jobAnnouncementBean);

            if (jobApplicationDAO.getJobApplication(m.getEmail(), jobAnnouncement) != null) {
                throw new ControllerLogicException("You have already applied for this job announcement");
            }

            JobApplication application = new JobApplication(
                    jobAnnouncement,
                    m,
                    raiseOffer
            );

            jobApplicationDAO.save(application);


        } catch (DAOException _) {
            throw new ControllerLogicException("Could not apply for job announcement");
        }

    }

    public boolean isMusicianAppliedToJob(JobAnnouncementBean jobAnnouncementBean, MusicianBean musician){

        JobAnnouncement job = BeanConverter.fromBeanToJobAnnouncement(jobAnnouncementBean);

        return jobApplicationDAO.getJobApplication(musician.getEmail(), job) != null;

    }


    public JobAnnouncementBean getUpdatedAnnouncement(JobAnnouncementBean currentJobAnnouncement) {

        JobAnnouncement job = BeanConverter.fromBeanToJobAnnouncement(currentJobAnnouncement);

        job = jobAnnouncementDAO.getFromCache(jobAnnouncementDAO.getUniqueId(job));

        return BeanConverter.fromJobAnnouncementToBean(job);

    }
}