package dao.announcement;

import engineering.persistency.DAOWithCache;
import model.JobAnnouncement;

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



    public abstract JobAnnouncement retrieveJobAnnouncementById(String id);
    public abstract String getUniqueId(JobAnnouncement job);
    public abstract List<JobAnnouncement> getAllJobAnnouncements();

    // these methods are abstract because the id will depend on the persistency mechanism at runtime;
    // this also implies that the id stored in the application that references its relative job will change based on
    // the configuration

}
