package view.musiciandashboard;

import bean.JobApplicationBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class MusicianDashboardGraphicController {

    private final ManageJobApplicationsController manageJobApplicationsController = new  ManageJobApplicationsController();

    protected Navigator navigator;

    public MusicianDashboardGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobApplicationBean> getApplications() {

        try {
            navigator.setJobApplications(manageJobApplicationsController.findMusicianJobApplications(
                    navigator.getSession())
            );

            return navigator.getJobApplications();

        } catch (ControllerLogicException e) {
            showError(e.getMessage());
        }
        catch (RuntimeException e) {
            showError("Internal error: "+e.getMessage());
        }

        return null;

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

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);

}
