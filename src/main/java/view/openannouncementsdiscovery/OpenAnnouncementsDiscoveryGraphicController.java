package view.openannouncementsdiscovery;

import bean.JobAnnouncementBean;
import bean.JobApplicationBean;
import controller.ManageJobApplicationController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.ArrayList;
import java.util.List;

public abstract class OpenAnnouncementsDiscoveryGraphicController {

    protected Navigator navigator;
    protected ManageJobApplicationController manageJobApplicationController =  new ManageJobApplicationController();

    private static final String INTERNAL_ERROR_MSG = "Internal error: ";

    protected OpenAnnouncementsDiscoveryGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobAnnouncementBean> findOpenJobAnnouncements(){

        try {

            if (navigator.getJobAnnouncements() == null){
                navigator.setJobAnnouncements(
                        manageJobApplicationController.findOpenJobAnnouncements()
                );
            }

            return navigator.getJobAnnouncements();

        } catch (ControllerLogicException e) {
            navigator.showError(e.getMessage());
        } catch (RuntimeException e) {
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
        }

        return new ArrayList<>();

    }

    protected void goToJobAnnouncement(){

        navigator.goToAnnouncementDetails();

    }

    protected void refreshUI(){
        navigator.setJobAnnouncements(null);
        this.start();
    }

    protected void backToMusicianDashboard(){

        navigator.goToMusicianDashboard();

    }

    protected boolean checkMusicianApplication(JobAnnouncementBean job){

        try{
            return manageJobApplicationController.isMusicianAppliedToJob(job, navigator.getMusician());
        }
        catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
        }
        catch(RuntimeException e){
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
        }
        return false;

    }

    protected void allStart(){
        navigator.setJobAnnouncements(manageJobApplicationController.findAllJobAnnouncements());
        this.start();
    }

    protected void openStart(){
        navigator.setJobAnnouncements(manageJobApplicationController.findOpenJobAnnouncements());
        this.start();

    }

    protected JobApplicationBean findMusicianJobApplication(JobAnnouncementBean job){
        try{
            return manageJobApplicationController.findMusicianJobApplication(navigator.getMusician(), job);
        } catch(ControllerLogicException e){
            navigator.showError(e.getMessage());
        } catch (RuntimeException e){
            navigator.showError(INTERNAL_ERROR_MSG + e.getMessage());
        }
        return null;
    }

    public abstract void start();

}
