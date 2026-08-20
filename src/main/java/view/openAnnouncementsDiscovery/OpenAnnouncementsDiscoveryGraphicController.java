package view.openAnnouncementsDiscovery;

import view.Navigator;

public abstract class OpenAnnouncementsDiscoveryGraphicController {

    protected Navigator navigator;

    public OpenAnnouncementsDiscoveryGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);
    //TODO

}
