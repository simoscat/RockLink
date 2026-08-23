package view.login;

import engineering.enums.Role;

import java.util.*;

import view.Navigator;

public class LoginGraphicControllerCLI extends LoginGraphicController {

    private Scanner scanner = new Scanner(System.in);

    public LoginGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        printHeader();
        boolean running = true;

        while (running) {
            System.out.println("--- Login Operations ---");
            System.out.println("[1] Musician Login");
            System.out.println("[2] Promoter Login");
            System.out.println("[3] Musician Registration");
            System.out.println("[4] Promoter Registration");
            System.out.println("[5] Exit");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    musicianLogin();
                    running = false;
                    break;
                case "2":
                    promoterLogin();
                    running = false;
                    break;
                case "3":
                    musicianSignUp();
                    running = false;
                    break;
                case "4":
                    promoterSignUp();
                    running = false;
                    break;
                case "5":
                    closeApp();
                    running = false;
                    break;
                default:
                    showError("Invalid choice.");
            }
        }
    }

    private void printHeader() {
        System.out.println("╔══════════════════════════════════╗\n");
        System.out.println("║          RockLink Login          ║\n");
        System.out.println("╚══════════════════════════════════╝\n");
    }

    private void musicianLogin() {
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();

        setRole(Role.MUSICIAN);
        doLogin();
    }

    private void promoterLogin() {
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();

        setRole(Role.PROMOTER);
        doLogin();
    }

    @Override
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    @Override
    public void showInfo(String message) {
        System.out.println("[INFO] " + message);
    }

}