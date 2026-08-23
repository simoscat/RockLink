package view.openannouncementsdiscovery;

import bean.JobAnnouncementBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class OpenAnnouncementsDiscoveryGraphicController {

    protected Navigator navigator;
    protected ManageJobApplicationsController manageJobApplicationsController =  new ManageJobApplicationsController();

    protected OpenAnnouncementsDiscoveryGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobAnnouncementBean> findOpenJobAnnouncements(){

        if (navigator.getJobAnnouncements() == null){
            navigator.setJobAnnouncements(
                    manageJobApplicationsController.findOpenJobAnnouncements()
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
            return manageJobApplicationsController.isMusicianAppliedToJob(job, navigator.getMusician());
        }
        catch(ControllerLogicException e){
            showError(e.getMessage());
        }
        catch(RuntimeException e){
            showError("Internal error: "+e.getMessage());
        }
        return false;

    }

    protected void allStart(){
        navigator.setJobAnnouncements(manageJobApplicationsController.findAllJobAnnouncements());
        this.start();
    }

    protected void openStart(){
        navigator.setJobAnnouncements(manageJobApplicationsController.findOpenJobAnnouncements());
        this.start();
    }

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);

}
