package view.applicationdetail;

import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

public abstract class JobApplicationDetailGraphicController {

    protected Navigator navigator;
    protected final ManageJobApplicationsController manageJobApplicationsController =  new ManageJobApplicationsController();

    protected JobApplicationDetailGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected void acceptApplication(){

        try {
            manageJobApplicationsController.acceptApplication(navigator.getCurrentJobApplication());

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
            manageJobApplicationsController.rejectApplication(navigator.getCurrentJobApplication());

            navigator.showInfo("Application rejected, going back to the job announcement.");

            start();
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

    public abstract void start();


}
