package view.musicianDashboard;

import view.Navigator;

public abstract class MusicianDashboardGraphicController {
    //TODO
    protected Navigator navigator;

    public MusicianDashboardGraphicController(Navigator navigator) {
        this.navigator = navigator;
    }

    public abstract void start();
    protected abstract void showError(String message);
    protected abstract void showInfo(String message);

}
