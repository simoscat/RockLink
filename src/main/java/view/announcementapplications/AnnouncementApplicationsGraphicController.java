package view.announcementapplications;

import bean.JobApplicationBean;
import controller.ManageJobApplicationController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.ArrayList;
import java.util.List;

public abstract class AnnouncementApplicationsGraphicController {

    private static final String INTERNAL_ERROR_MSG = "Internal error: ";

    protected ManageJobApplicationController manageJobApplicationController = new ManageJobApplicationController();

    protected Navigator navigator;

    protected AnnouncementApplicationsGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobApplicationBean> getJobApplications(){

        try {
            navigator.setJobApplications(manageJobApplicationController.findJobAnnouncementApplications(
                    navigator.getCurrentJobAnnouncement()
            ));

            return navigator.getJobApplications();
        } catch (ControllerLogicException e) {
            navigator.showError(e.getMessage());
        } catch (RuntimeException e){
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
        }

        return new ArrayList<>();

    }

    protected void jumpToJobApplication(){

        navigator.goToApplicationDetails();

    }

    protected void backToJobAnnouncement(){
        navigator.goToAnnouncementDetails();
    }

    protected void acceptJobApplication(){

        try {
            manageJobApplicationController.acceptApplication(navigator.getCurrentJobApplication());

            navigator.showInfo("Application was accepted! Going back to dashboard");

            navigator.goToPromoterDashboard();
        }
        catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
            this.start();
        }
        catch(RuntimeException e){
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
            this.start();
        }
    }

    protected void rejectJobApplication(){

        try{
            manageJobApplicationController.rejectApplication(navigator.getCurrentJobApplication());

            navigator.showInfo("Application was rejected.");

            start();
        }
        catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
            this.start();
        }
        catch(RuntimeException e){
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
            this.start();
        }

    }

    protected void refreshUI(){

        this.start();

    }

    public abstract void start();


}
