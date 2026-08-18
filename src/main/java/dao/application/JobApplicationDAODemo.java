package dao.application;

import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import engineering.enums.ApplicationStatus;
import exception.DAOException;
import model.JobAnnouncement;
import model.JobApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAODemo extends JobApplicationDAO {

    List<JobApplication> jobApplications;

    public JobApplicationDAODemo() {

        JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

        List<JobAnnouncement> jobAnnouncements = jobAnnouncementDAO.getAllJobAnnouncements();

        JobAnnouncement job =  jobAnnouncements.getFirst();

        JobApplication jobApp = new JobApplication(
                jobAnnouncementDAO.getUniqueId(job),
                "anna.muscatello@gmail.com",
                ApplicationStatus.PENDING,
                new BigDecimal(30)
        );

        jobApplications = new ArrayList<>();
        jobApplications.add(jobApp);

    }

    @Override
    public List<JobApplication> getAllJobApplicationsFromEmail(String email) {

        List<JobApplication> jobApplications = new ArrayList<>();

        for (JobApplication jobApplication : this.jobApplications) {

            if (jobApplication.getCandidateEmail().equals(email)) {
                jobApplications.add(jobApplication);
            }

        }

        if (jobApplications.isEmpty()) {
            throw new DAOException("No applications found for email: "+email);
        }
        return jobApplications;
    }

    @Override
    protected JobApplication retrieveJobApplicationById(String id) {

        for (JobApplication jobApplication : jobApplications) {

            if (getUniqueId(jobApplication).equals(id)) {
                return jobApplication;
            }

        }

        throw new DAOException("No applications found for id: "+ id);

    }

    @Override
    protected String getUniqueId(JobApplication jobApp) {
        return jobApp.getCandidateEmail() + "~" + jobApp.getApplicationAnnouncementId();
    }

    @Override
    protected void saveToPersistency(JobApplication obj) {
        //nothing to do in demo
    }
}
