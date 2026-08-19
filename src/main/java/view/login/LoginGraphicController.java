package view.login;

import engineering.enums.Role;
import view.Navigator;

import java.util.Scanner;

public abstract class LoginGraphicController {

    // Navigator used to move between screens
    protected Navigator navigator;
    protected Role role;

    protected LoginGraphicController(Navigator navigator){
        this.navigator = navigator;
    }

    protected Navigator getNavigator(){
        return this.navigator;
    }

    protected void setNavigator(Navigator navigator){
        this.navigator = navigator;
    }

    // UI hook methods the concrete graphic controller must implement
    // to show errors and informational messages to the user.
    public abstract void showError(String message);
    public abstract void showInfo(String message);
    public abstract void start(Scanner scanner);

}
