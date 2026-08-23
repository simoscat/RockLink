package view.promoterdashboard;

import bean.JobAnnouncementBean;
import controller.ManageJobApplicationsController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class PromoterDashboardGraphicController {

    protected Navigator navigator;

    protected PromoterDashboardGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    protected List<JobAnnouncementBean> getPromoterJobAnnouncements(){

        try {

            List<JobAnnouncementBean> jobAnnouncements = new ManageJobApplicationsController().
                    findPromoterPublishedJobAnnouncements(navigator.getSession());

            navigator.setJobAnnouncements(jobAnnouncements);

        } catch (ControllerLogicException e) {
            navigator.showError(e.getMessage());
            start();
        }
        catch (RuntimeException e){
            navigator.showError("Internal error: "+ e.getMessage());
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


}
