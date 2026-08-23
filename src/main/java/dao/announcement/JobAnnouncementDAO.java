package dao.announcement;

import engineering.enums.JobAnnouncementStatus;
import dao.DAOWithCache;
import model.JobAnnouncement;

import java.util.ArrayList;
import java.util.List;

public abstract class JobAnnouncementDAO extends DAOWithCache<JobAnnouncement> {


    @Override
    public String getKey(JobAnnouncement job) {
        return getUniqueId(job);
    }


    public JobAnnouncement getAnnouncementFromId(String id){

        if (this.isCached(id)){
            return this.getFromCache(id);
        }

        else{
            JobAnnouncement job = this.retrieveJobAnnouncementById(id);
            addToCache(job);
            return job;
        }

    }

    public List<JobAnnouncement> getAllJobAnnouncements(){
        List<JobAnnouncement> jobAnnouncements = this.retrieveAllJobAnnouncements();

        checkForCache(jobAnnouncements);

        return jobAnnouncements;
    }

    public List<JobAnnouncement> getAllOpenJobAnnouncements(){

        List<JobAnnouncement> jobAnnouncements = this.retrieveAllOpenJobAnnouncements();
        checkForCache(jobAnnouncements);
        return jobAnnouncements;

    }

    public List<JobAnnouncement> getAllPromoterAnnouncementsFromEmail(String email){

        List<JobAnnouncement> jobAnnouncements = retrieveAllPromoterAnnouncementsFromEmail(email);

        checkForCache(jobAnnouncements);

        return jobAnnouncements;

    }


    private void checkForCache(List<JobAnnouncement> jobAnnouncements){
        for (JobAnnouncement jobAnnouncement : jobAnnouncements){
            if (!isCached(jobAnnouncement)){
                addToCache(jobAnnouncement);
            }
        }
    }


    protected abstract List<JobAnnouncement> retrieveAllPromoterAnnouncementsFromEmail(String email);
    protected abstract JobAnnouncement retrieveJobAnnouncementById(String id);
    public abstract String getUniqueId(JobAnnouncement job);
    protected abstract List<JobAnnouncement> retrieveAllJobAnnouncements();

    protected List<JobAnnouncement> retrieveAllOpenJobAnnouncements() {

        List<JobAnnouncement> allJobs = getAllJobAnnouncements();
        List<JobAnnouncement> openJobAnnouncements = new ArrayList<>();

        for (JobAnnouncement jobAnnouncement : allJobs) {

            if (jobAnnouncement.getStatus().equals(JobAnnouncementStatus.OPEN)) {
                openJobAnnouncements.add(jobAnnouncement);
            }

        }

        return openJobAnnouncements;
    }

    //Important: retrieveAllOpenJobAnnouncements is implemented like this to avoid code duplication.
    // if we were using different persistency mechanisms that implement this differently and (probably) more
    // efficiently, we should override this method

}
