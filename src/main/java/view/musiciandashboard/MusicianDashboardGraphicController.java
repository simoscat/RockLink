package view.musiciandashboard;

import bean.JobApplicationBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.ArrayList;
import java.util.List;

public abstract class MusicianDashboardGraphicController {

    private final ManageJobApplicationsController manageJobApplicationsController = new  ManageJobApplicationsController();

    protected Navigator navigator;

    protected MusicianDashboardGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobApplicationBean> getApplications() {

        try {
            navigator.setJobApplications(manageJobApplicationsController.findMusicianJobApplications(
                    navigator.getSession())
            );

        } catch (ControllerLogicException e) {
            navigator.showError(e.getMessage());
            navigator.setJobApplications(new ArrayList<>());
        }
        catch (RuntimeException e) {
            navigator.showError("Internal error: "+e.getMessage());
            navigator.setJobApplications(new ArrayList<>());
        }

        return navigator.getJobApplications();

    }

    protected void refreshDashboard(){
        start();
    }

    protected void logout(){

        navigator.restart();

    }

    protected void goToJobAnnouncement(){

        navigator.goToAnnouncementDetails();

    }

    protected void viewOpenAnnouncements(){

        navigator.goToOpenAnnouncementsDiscovery();

    }

    protected void viewNotifications(){

        navigator.goToNotifications();

    }

    public abstract void start();

}
