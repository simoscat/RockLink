package view.login;

import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import controller.LoginController;
import engineering.enums.Role;
import exception.ControllerLogicException;
import view.Navigator;


public abstract class LoginGraphicController {

    // Navigator used to move between screens
    protected Navigator navigator;
    protected Role role;
    protected String email;
    protected String password;

    protected LoginGraphicController(Navigator navigator){
        this.navigator = navigator;
    }

    protected void setRole(Role role) { this.role = role; }

    protected Role getRole() {return this.role;}

    protected Navigator getNavigator(){
        return this.navigator;
    }

    protected void setNavigator(Navigator navigator){
        this.navigator = navigator;
    }

    protected void doLogin(){

        LoginController loginController = new LoginController();

        if (getRole().equals(Role.MUSICIAN)){
            try {

                MusicianBean mb = new MusicianBean(email, password);

                SessionBean session = loginController.musicianLogIn(mb);

                navigator.setMusician(mb);
                navigator.setSession(session);

                navigator.goToMusicianDashboard();

            } catch (ControllerLogicException | IllegalArgumentException e) {
                navigator.showError(e.getMessage());
                start();
            }
            catch (RuntimeException e){
                navigator.showError("Internal error: "+ e.getMessage());
                start();
            }
        }

        else if (getRole().equals(Role.PROMOTER)){
            try{

                PromoterBean pb = new PromoterBean(email, password);

                SessionBean session = loginController.promoterLogin(pb);

                navigator.setPromoter(pb);
                navigator.setSession(session);

                navigator.goToPromoterDashboard();

            } catch (ControllerLogicException e) {
                navigator.showError(e.getMessage());
                start();
            }
            catch (RuntimeException e){
                navigator.showError("Internal error: "+ e.getMessage());
                start();
            }
        }

    }

    public void closeApp(){
        if (navigator != null) navigator.close();
    }

    public abstract void start();

    public void musicianSignUp(){
        navigator.goToMusicianRegistration();
    }

    public void promoterSignUp(){
        navigator.goToPromoterRegistration();
    }


}
