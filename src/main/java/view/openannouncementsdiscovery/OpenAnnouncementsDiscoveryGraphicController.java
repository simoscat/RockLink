package view.openannouncementsdiscovery;

import bean.JobAnnouncementBean;
import controller.ManageJobApplicationController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class OpenAnnouncementsDiscoveryGraphicController {

    protected Navigator navigator;
    protected ManageJobApplicationController manageJobApplicationController =  new ManageJobApplicationController();

    protected OpenAnnouncementsDiscoveryGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobAnnouncementBean> findOpenJobAnnouncements(){

        if (navigator.getJobAnnouncements() == null){
            navigator.setJobAnnouncements(
                    manageJobApplicationController.findOpenJobAnnouncements()
            );

        }
        return navigator.getJobAnnouncements();

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
            navigator.showError("Internal error: "+e.getMessage());
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

    public abstract void start();

}
