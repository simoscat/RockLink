package dao.announcement;

import engineering.persistency.DAOWithCache;
import model.Announcement;

public abstract class AnnouncementDAO extends DAOWithCache<Announcement> {

    @Override
    public String getKey(Announcement obj) {
        return obj.getId();
    }

    public Announcement getAnnouncementById(String id) {
        if (isCached(id)) {
            return getFromCache(id);
        } else {
            Announcement announcement = retrieveAnnouncementById(id);
            addToCache(announcement);
            return announcement;
        }
    }

    protected abstract Announcement retrieveAnnouncementById(String id);

    protected abstract void saveToPersistency(Announcement announcement);
}
