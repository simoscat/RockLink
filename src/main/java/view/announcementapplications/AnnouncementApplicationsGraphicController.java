package view.announcementapplications;

import bean.JobApplicationBean;
import controller.ManageJobApplicationController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class AnnouncementApplicationsGraphicController {

    protected ManageJobApplicationController manageJobApplicationController = new ManageJobApplicationController();

    protected Navigator navigator;

    protected AnnouncementApplicationsGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobApplicationBean> getJobApplications(){

        navigator.setJobApplications(manageJobApplicationController.findJobAnnouncementApplications(
                navigator.getCurrentJobAnnouncement()
        ));

        return navigator.getJobApplications();

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
            navigator.showError("Internal error: "+ e.getMessage());
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
            navigator.showError("Internal error: "+ e.getMessage());
            this.start();
        }

    }

    protected void refreshUI(){

        this.start();

    }

    public abstract void start();


}
