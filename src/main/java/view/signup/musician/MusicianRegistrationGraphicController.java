package view.signup.musician;

import bean.InstrumentBean;
import bean.MusicianBean;
import bean.SessionBean;
import controller.LoginController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.List;

public abstract class MusicianRegistrationGraphicController {

    protected String name;
    protected String surname;
    protected String stageName;
    protected String gender;
    protected String email;
    protected String password;
    protected List<InstrumentBean> instruments;


    protected Navigator navigator;

    protected MusicianRegistrationGraphicController(Navigator n) {
        this.navigator = n;
    }


    protected void doRegistration(){

        try{

            if (this.name.isBlank() || this.surname.isBlank() || this.stageName.isBlank() ||
            this.gender.isBlank() || this.email.isBlank() || this.password.isBlank()){
                throw new IllegalArgumentException("Can't have blank fields");
            }

            LoginController loginController = new LoginController();

            MusicianBean mb = new MusicianBean(this.name, this.surname,
                    this.email, this.gender, this.password, this.stageName, this.instruments);

            SessionBean sb = loginController.musicianRegistration(mb);

            navigator.setMusician(mb);
            navigator.setSession(sb);

            navigator.showInfo("Registration succesful, welcome "+ sb.getMusician().getName()+"! You will be automatically logged in");

            navigator.goToMusicianDashboard();

        }
        catch (ControllerLogicException | IllegalArgumentException e){
            navigator.showError(e.getMessage());
            navigator.restart();
        }

        catch (RuntimeException e){
            navigator.showError("Internal error: "+ e.getMessage());
            navigator.restart();
        }

    }

    public abstract void start();


}
