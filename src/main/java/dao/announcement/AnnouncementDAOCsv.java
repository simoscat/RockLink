package dao.announcement;

import model.Announcement;

public class AnnouncementDAOCsv extends AnnouncementDAO {

    /*
    Id structure:
    JOB_<id>
     */

    @Override
    protected Announcement retrieveAnnouncementById(String id) {
        return null;
    }

    @Override
    protected void saveToPersistency(Announcement announcement) {

    }

    //TODO
}
