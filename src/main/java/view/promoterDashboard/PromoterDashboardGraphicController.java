package view.promoterDashboard;

import bean.JobAnnouncementBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.ArrayList;
import java.util.List;

public abstract class PromoterDashboardGraphicController {

    protected Navigator navigator;

    public PromoterDashboardGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobAnnouncementBean> getPromoterJobAnnouncements(){

        try {

            List<JobAnnouncementBean> jobAnnouncements = new ManageJobApplicationsController().
                    findPromoterPublishedJobAnnouncements(navigator.getSession());

            navigator.setJobAnnouncements(jobAnnouncements);

        } catch (ControllerLogicException e) {
            showError(e.getMessage());
            start();
        }
        catch (RuntimeException e){
            showError("Internal error: "+ e.getMessage());
            start();
        }

        return navigator.getJobAnnouncements();

    }

    protected void goToJobAnnouncement(JobAnnouncementBean jobAnnouncement){

        navigator.setCurrentJobAnnouncement(jobAnnouncement);
        navigator.goToAnnouncementDetails();

    }

    protected void doLogout(){
        navigator.restart();
    }

    protected void goToCreateAnnouncement(){
        navigator.goToCreateAnnouncement();
    }

    protected void reloadDashboard(){
        start();
    }

    public abstract void start();
    public abstract void showError(String message);
    public abstract void showInfo(String message);

}
