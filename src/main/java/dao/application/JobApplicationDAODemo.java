package dao.application;

import dao.announcement.JobAnnouncementDAO;
import dao.factories.DAOFactory;
import model.ApplicationStatus;
import model.Artist;
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

        JobAnnouncement job = jobAnnouncements.getFirst();


        Artist a = DAOFactory.getInstance().getArtistDAO().getArtistByEmail("anna.muscatello@gmail.com");

        JobApplication jobApp = new JobApplication(
                job,
                a,
                ApplicationStatus.PENDING,
                new BigDecimal(50)
        );

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

        List<JobApplication> toRet = new ArrayList<>();

        for (JobApplication jobApplication : this.jobApplications) {

            if (jobApplication.whoIsCandidate().getEmail().equals(email)) {
                toRet.add(jobApplication);
            }

        }

        return toRet;

    }

    @Override
    protected List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement) {

        List<JobApplication> jobs = new ArrayList<>();

        for (JobApplication jobApplication : jobApplications) {

            JobAnnouncement job = jobApplication.whichJobAnnouncement();

            if (job.getPublisher().getEmail().equals(jobAnnouncement.getPublisher().getEmail())
            &&
            job.getAnnouncementPublishDate().equals(jobAnnouncement.getAnnouncementPublishDate())) {
                jobs.add(jobApplication);
            }

        }

        return jobs;
    }


    @Override
    protected void saveToPersistency(JobApplication obj) {

        boolean found = false;

        for (int i = 0; i < jobApplications.size(); i++) {

            if (getUniqueId(jobApplications.get(i)).equals(getUniqueId(obj))) {

                found = true;
                jobApplications.set(i, obj);

            }

        }

        if (!found){
            jobApplications.add(obj);
        }

    }
}
