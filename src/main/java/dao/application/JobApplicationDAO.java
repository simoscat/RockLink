package dao.application;

import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import engineering.persistency.DAOWithCache;
import model.JobAnnouncement;
import model.JobApplication;

import java.util.List;

public abstract class JobApplicationDAO extends DAOWithCache<JobApplication> {

    protected static JobAnnouncementDAO jobAnnouncementDAO;

    public JobApplicationDAO() {
        jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();
    }

    @Override
    public String getKey(JobApplication jobApp) {
        return getUniqueId(jobApp);
    }

    public List<JobApplication> getAllMusicianJobApplicationsFromEmail(String email) {

        List<JobApplication> jobApplications = retrieveAllJobApplicationsFromEmail(email);

        for (JobApplication jobApplication : jobApplications) {
            if (!isCached(jobApplication)){
                addToCache(jobApplication);
            }
        }

        return jobApplications;

    }

    public List<JobApplication> getAllJobAnnouncementApplications(JobAnnouncement jobAnnouncement) {

        List<JobApplication> applications = retrieveAllJobApplicationsFromJob(jobAnnouncement);

        for (JobApplication jobApplication : applications) {

            if (!isCached(jobApplication)){
                addToCache(jobApplication);
            }

        }

        return applications;

    }

    public JobApplication getJobApplication (String candidateEmail, JobAnnouncement jobAnnouncement){

        if (!isCached(getUniqueId(candidateEmail, jobAnnouncement))){
            JobApplication jobApp = retrieveJobApplication(candidateEmail, jobAnnouncement);
            addToCache(jobApp);
            return jobApp;
        }

        else{
            return getFromCache(getUniqueId(candidateEmail, jobAnnouncement));
        }
    }

    protected abstract JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement);

    protected abstract List<JobApplication> retrieveAllJobApplicationsFromEmail(String email);

    protected abstract List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement);

    protected String getUniqueId(String candidateEmail, JobAnnouncement jobAnnouncement){
        return candidateEmail + "~" + DAOFactory.getInstance().getJobAnnouncementDAO().getUniqueId(jobAnnouncement);
    }

    protected String getUniqueId(JobApplication jobApplication){
        return getUniqueId(jobApplication.whoIsCandidate().getEmail(), jobApplication.whichJobAnnouncement());
    }
    //just like JobAnnouncement, the unique id will be determined by the specific persistence mechanism
}
