package view.applicationdetail;

import bean.JobApplicationBean;
import controller.ManageJobApplicationController;
import exception.ControllerLogicException;
import view.Navigator;

public abstract class JobApplicationDetailGraphicController {

    protected Navigator navigator;
    protected final ManageJobApplicationController manageJobApplicationController =  new ManageJobApplicationController();

    protected JobApplicationDetailGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected void acceptApplication(){

        try {
            manageJobApplicationController.acceptApplication(navigator.getCurrentJobApplication());

            navigator.showInfo("Application was accepted! Going back to dashboard");

            navigator.goToPromoterDashboard();
        }
        catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
            start();
        }
        catch(RuntimeException e){
            navigator.showError("Internal error: "+ e.getMessage());
            start();
        }
    }

    protected void rejectApplication(){

        try{
            manageJobApplicationController.rejectApplication(navigator.getCurrentJobApplication());

            navigator.showInfo("Application rejected, going back to the job announcement.");

            navigator.goToAnnouncementDetails();
        }
        catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
            start();
        }
        catch(RuntimeException e){
            navigator.showError("Internal error: "+ e.getMessage());
            start();
        }

    }

    protected void backToJobApplications(){
        navigator.goToAnnouncementApplications();
    }

    protected JobApplicationBean getCurrentJobApplication(){

        return navigator.getCurrentJobApplication();

    }

    public abstract void start();


}
