package dao.application;

import engineering.persistency.DAOWithCache;
import model.JobApplication;

import java.util.List;

public abstract class JobApplicationDAO extends DAOWithCache<JobApplication> {

    @Override
    public String getKey(JobApplication jobApp) {
        return getUniqueId(jobApp);
    }

    public JobApplication getJobApplicationById(String id) {

        if (this.isCached(id)){
            return this.getFromCache(id);
        }

        else{
            JobApplication jobApp = retrieveJobApplicationById(id);
            this.addToCache(jobApp);
            return jobApp;
        }

    }

    public List<JobApplication> getAllJobApplicationsFromEmail(String email) {

        List<JobApplication> jobApplications = retrieveAllJobApplicationsFromEmail(email);

        for (JobApplication jobApplication : jobApplications) {
            if (!isCached(jobApplication)){
                addToCache(jobApplication);
            }
        }

        return jobApplications;

    }

    protected abstract List<JobApplication> retrieveAllJobApplicationsFromEmail(String email);

    protected abstract JobApplication retrieveJobApplicationById(String id);

    protected abstract String getUniqueId(JobApplication jobApp);
    //just like JobAnnouncement, the unique id will be determined by the specific persistence mechanism
}
