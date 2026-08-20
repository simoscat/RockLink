package dao.application;

import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import engineering.enums.ApplicationStatus;
import engineering.enums.Gender;
import engineering.enums.Mastery;
import exception.DAOException;
import model.Instrument;
import model.JobAnnouncement;
import model.JobApplication;
import model.Musician;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAODemo extends JobApplicationDAO {

    List<JobApplication> jobApplications;

    public JobApplicationDAODemo() {

        JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

        List<JobAnnouncement> jobAnnouncements = jobAnnouncementDAO.getAllJobAnnouncements();

        JobAnnouncement job =  jobAnnouncements.getFirst();

        Instrument i1 = new Instrument("Electric guitar", Mastery.MASTER);

        List<Instrument> iList = new ArrayList<>();

        iList.add(i1);


        Musician m = new Musician("Anna", "Muscatello",
                "Muschio", "anna.muscatello@gmail.com", Gender.FEMALE,
                iList);

        JobApplication jobApp = new JobApplication(
                job,
                m);

        jobApplications = new ArrayList<>();
        jobApplications.add(jobApp);

    }

    @Override
    protected JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement) {

        for (JobApplication jobApplication : this.jobApplications) {

            if (jobApplication.whoIsCandidate().getEmail().equals(candidateEmail) &&
            jobApplication.whichJobAnnouncement().equals(jobAnnouncement)) {

                if (!isCached(getUniqueId(jobApplication))) {
                    this.addToCache(jobApplication);
                }

                return jobApplication;
            }

        }

        return null;

    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {

        List<JobApplication> jobApplications = new ArrayList<>();

        for (JobApplication jobApplication : this.jobApplications) {

            if (jobApplication.whoIsCandidate().getEmail().equals(email)) {
                jobApplications.add(jobApplication);

                if (!isCached(getUniqueId(jobApplication))) {
                    this.addToCache(jobApplication);
                }
            }

        }

        return jobApplications;
    }

    @Override
    protected List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement) {

        List<JobApplication> jobs = new ArrayList<>();

        for (JobApplication jobApplication : jobApplications) {

            if (jobApplication.whichJobAnnouncement().equals(jobAnnouncement)) {
                jobs.add(jobApplication);
            }

        }

        return jobs;
    }


    @Override
    protected void saveToPersistency(JobApplication obj) {
        jobApplications.add(obj);
    }
}
