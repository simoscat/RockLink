package view.announcementApplications;

import view.Navigator;

public abstract class AnnouncementApplicationsGraphicController {
    //TODO
    protected Navigator navigator;

    public AnnouncementApplicationsGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);

}
