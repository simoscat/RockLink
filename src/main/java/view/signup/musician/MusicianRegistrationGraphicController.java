package view.signup.musician;

import bean.InstrumentBean;
import bean.MusicianBean;
import bean.SessionBean;
import controller.LoginController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;
import java.util.Map;

public abstract class MusicianRegistrationGraphicController {

    protected String name;
    protected String surname;
    protected String stageName;
    protected String gender;
    protected String email;
    protected String password;
    protected List<InstrumentBean> instruments;


    protected Navigator navigator;

    public MusicianRegistrationGraphicController(Navigator n) {
        this.navigator = n;
    }


    protected void doRegistration(){

        try{

            LoginController loginController = new LoginController();

            MusicianBean mb = new MusicianBean(this.name, this.surname,
                    this.email, this.gender, this.password, this.stageName, this.instruments);

            SessionBean sb = loginController.musicianRegistration(mb);

            navigator.setMusician(mb);
            navigator.setSession(sb);

            showInfo("Registration succesful, welcome "+ sb.getMusician().getName()+"! You will be automatically logged in");

            navigator.goToMusicianDashboard();

        }
        catch (ControllerLogicException | IllegalArgumentException e){
            showError(e.getMessage());
            start();
        }
        catch (RuntimeException e){
            showError("Internal error: "+ e.getMessage());
            start();
        }

    }

    public abstract void showError(String message);
    public abstract void showInfo(String message);
    public abstract void start();


}
