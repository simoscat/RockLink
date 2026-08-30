package view.createannouncement;

import bean.JobAnnouncementBean;
import controller.PublishJobAnnouncementController;
import exception.ControllerLogicException;
import view.Navigator;

public abstract class CreateAnnouncementGraphicController {

    protected Navigator navigator;

    protected CreateAnnouncementGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected void backToDashboard(){
        this.navigator.goToPromoterDashboard();
    }

    protected void publishAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        try{

            PublishJobAnnouncementController jobAnnouncementController = new PublishJobAnnouncementController();

            jobAnnouncementController.publishJobAnnouncement(jobAnnouncementBean);

            navigator.showInfo("Announcement was successfully published! Going back to dashboard");

        }
        catch (ControllerLogicException e){
            navigator.showError(e.getMessage()+"\nAnnouncement not published. Going back to dashboard.");
        }
        catch (RuntimeException e){
            navigator.showError("Internal error: " + e.getMessage()+"\nAnnouncement not published. Going back to dashboard");
        }

        navigator.goToPromoterDashboard();

    }


    protected abstract void start();

}
