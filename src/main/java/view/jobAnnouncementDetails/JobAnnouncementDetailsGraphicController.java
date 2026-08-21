package view.jobAnnouncementDetails;

import bean.JobApplicationBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.math.BigDecimal;

//TODO
public abstract class JobAnnouncementDetailsGraphicController {

    protected Navigator navigator;

    protected ManageJobApplicationsController jobAppController = new ManageJobApplicationsController();

    public JobAnnouncementDetailsGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);

    protected boolean hasMusicianAlreadyApplied(){

        return isMusician() && jobAppController.isMusicianAppliedToJob(
                navigator.getCurrentJobAnnouncement(),
                navigator.getMusician()
        );

    }

    protected JobApplicationBean getMusicianApplication(){

        return jobAppController.findMusicianJobApplication(
                navigator.getMusician(),
                navigator.getCurrentJobAnnouncement()
        );


    }

    protected void applyMusicianForJob(BigDecimal raiseOffer){

        try{

            if (isMusician()) {

                jobAppController.applyForJobAnnouncement(
                        navigator.getCurrentJobAnnouncement(),
                        navigator.getMusician(),
                        raiseOffer
                );

            }

            showInfo("Application sent! Going back to previous screen");

            backToPreviousScreen();

        }
        catch (ControllerLogicException e){
            showError(e.getMessage());
            start();
        }
        catch (RuntimeException e){
            showError("Internal error: "+e.getMessage());
            start();
        }
    }

    protected void backToPreviousScreen(){

        navigator.goBack();

    }

    protected void closeJobAnnouncement(){

        try{

            jobAppController.closeJobAnnouncement(navigator.getCurrentJobAnnouncement());
            freshStart();

        }
        catch (ControllerLogicException e){
            showError(e.getMessage());
            start();
        }
        catch(RuntimeException e){
            showError("Internal error: "+e.getMessage());
            start();
        }

    }

    protected void viewJobApplications(){

        navigator.goToAnnouncementApplications();

    }

    protected boolean isMusician(){

        return navigator.getMusician() != null;

    }

    protected boolean isPromoter(){

        return navigator.getPromoter() != null;

    }

    private void freshStart(){

        try {
            navigator.setCurrentJobAnnouncement(
                    jobAppController.getUpdatedAnnouncement(navigator.getCurrentJobAnnouncement())
            );
        } catch (ControllerLogicException e) {
            showError(e.getMessage());
        }
        catch (RuntimeException e){
            showError("Internal error: "+e.getMessage());
        }

        start();


    }

}
