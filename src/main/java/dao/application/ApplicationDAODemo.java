package dao.application;

import dao.announcement.AnnouncementDAO;
import dao.factories.DAOFactory;
import engineering.enums.ApplicationStatus;
import exception.DAOException;
import model.Announcement;
import model.Application;

import java.util.ArrayList;
import java.util.List;

//TODO
public class ApplicationDAODemo extends ApplicationDAO {

    @Override
    protected List<Application> getApplicationsFromAnnouncement(Announcement announcement) {

        if (id.equals("0")){
            List<Application> applications = new ArrayList<>();

            AnnouncementDAO announcementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

            Application app = new Application(
                    announcementDAO.getAnnouncementById(id),
                    "anna.muscatello@gmail.com",
                    ApplicationStatus.PENDING
            );

            applications.add(app);

            return applications;
        }

        throw new DAOException("No applications found for this announcement");
    }

    @Override
    protected void saveToPersistency(Application application) {
        //nothing to do in demo
    }
}
