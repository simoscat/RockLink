package dao.application;

import engineering.persistency.DAOWithCache;
import exception.DAOException;
import model.Announcement;
import model.Application;

import java.util.List;

public abstract class ApplicationDAO extends DAOWithCache<Application> {

    @Override
    public String getKey(Application application) {
        return application.getApplicationAnnouncement().getAnnouncementPublishDate().toString() + "_" +
                application.getApplicationAnnouncement().getPublisher().getEmail() + "_" +
                application.getCandidateEmail();

        // the application key is built as follows:
        // publishDateAndTime_publisherEmail_candidateEmail
    }

    public abstract List<Application> getApplicationsFromAnnouncement(Announcement announcement) throws DAOException;


    @Override
    public void save(Application application) throws DAOException{
        saveToPersistency(application);
        addToCache(application);
    }

    protected abstract void saveToPersistency(Application application);

}
