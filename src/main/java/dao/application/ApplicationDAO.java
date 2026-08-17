package dao.application;

import engineering.persistency.DAOWithCache;
import exception.DAOException;
import model.Announcement;
import model.Application;

import java.util.List;

public abstract class ApplicationDAO extends DAOWithCache<Application> {

    @Override
    public String getKey(Application obj) {
        return obj.getId();
    }

    public List<Application> getApplicationsFromAnnouncement(Announcement announcement) throws DAOException{
        return retrieveApplicationsFromAnnouncement(announcement.getId());
    }


    @Override
    public void save(Application application) throws DAOException{
        saveToPersistency(application);
        addToCache(application);
    }

    protected abstract List<Application> retrieveApplicationsFromAnnouncement(String id);
    protected abstract void saveToPersistency(Application application);

}
