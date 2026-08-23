package view.jobannouncementdetails;

import bean.JobApplicationBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.math.BigDecimal;

public abstract class JobAnnouncementDetailsGraphicController {

    private static final String INTERNAL_ERROR_PREFIX = "Internal error: ";

    protected Navigator navigator;

    protected ManageJobApplicationsController jobAppController = new ManageJobApplicationsController();

    protected JobAnnouncementDetailsGraphicController(Navigator navigator) {
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
            showError(INTERNAL_ERROR_PREFIX+e.getMessage());
            start();
        }
    }

    protected void backToPreviousScreen(){

        navigator.goBack();

    }

    protected void backToPromoterDashboard(){
        navigator.goToPromoterDashboard();
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
            showError(INTERNAL_ERROR_PREFIX+e.getMessage());
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
            showError(INTERNAL_ERROR_PREFIX+e.getMessage());
        }

        start();


    }

}
