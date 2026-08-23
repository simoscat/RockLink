package view.signup.promoter;

import bean.PromoterBean;
import bean.SessionBean;
import controller.LoginController;
import exception.ControllerLogicException;
import view.Navigator;

import java.util.Map;

public abstract class PromoterRegistrationGraphicController {

    protected String name;
    protected String surname;
    protected String gender;
    protected String email;
    protected String password;
    protected Map<String, String> contacts;

    protected Navigator navigator;

    protected PromoterRegistrationGraphicController(Navigator n) {
        this.navigator = n;
    }


    protected void doRegistration(){

        try{

            if (this.name.isBlank() || this.surname.isBlank() ||
                    this.email.isBlank() || this.password.isBlank() ||
            this.contacts.isEmpty()){
                throw new IllegalArgumentException("Can't have blank fields");
            }

            LoginController loginController = new LoginController();

            PromoterBean pb = new PromoterBean(name, surname, email, gender, password, contacts);

            SessionBean sb = loginController.promoterRegistration(pb);

            navigator.setPromoter(pb);
            navigator.setSession(sb);

            navigator.showInfo("Registration succesful, welcome "+ sb.getPromoter().getName()+"! You will be automatically logged in");

            navigator.goToPromoterDashboard();

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
