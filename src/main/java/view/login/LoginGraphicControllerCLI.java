package view.login;

import bean.InstrumentBean;
import controller.LoginController;
import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import engineering.EmailChecker;
import engineering.PasswordChecker;
import engineering.enums.Screen;
import exception.ControllerLogicException;
import exception.WrongCredentialsException;

import java.util.*;

import view.Navigator;

public class LoginGraphicControllerCLI extends LoginGraphicController {

    private final LoginController loginController;
    private Scanner scanner;

    public LoginGraphicControllerCLI(Navigator navigator) {
        super(navigator);
        this.loginController = new LoginController();
    }

    @Override
    public void start(Scanner s) {

        this.scanner = s;

        printHeader();
        boolean running = true;

        while (running) {
            System.out.println("\n--- Login Operations ---");
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
                    musicianRegistration();
                    running = false;
                    break;
                case "4":
                    promoterRegistration();
                    running = false;
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    showError("Invalid choice.");
            }
        }
    }

    private void printHeader() {
        System.out.println("  _____            _    _      _       _    ");
        System.out.println(" |  __ \\          | |  | |    (_)     | |   ");
        System.out.println(" | |__) |___   ___| | _| |     _ _ __ | | __");
        System.out.println(" |  _  // _ \\ / __| |/ / |    | | '_ \\| |/ /");
        System.out.println(" | | \\ \\ (_) | (__|   <| |____| | | | |   < ");
        System.out.println(" |_|  \\_\\___/ \\___|_|\\_\\______|_|_| |_|_|\\_\\");
        System.out.println("                                            ");
    }

    private void musicianLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            MusicianBean mb = new MusicianBean(email, password);
            SessionBean session = loginController.musicianLogIn(mb);
            showInfo("Login successful! Welcome " + session.getMusician().getName());
            navigator.setCurrentScreen(Screen.MUSICIAN_DASHBOARD);
            navigator.nextScreen();
        } catch (WrongCredentialsException | ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
            start(scanner);
        }
    }

    private void promoterLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            PromoterBean pb = new PromoterBean(email, password);
            SessionBean session = loginController.promoterLogin(pb);
            showInfo("Login successful! Welcome " + session.getPromoter().getName());
            navigator.setCurrentScreen(Screen.PROMOTER_DASHBOARD);
            navigator.nextScreen();
        } catch (WrongCredentialsException | ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
            start(scanner);
        }
    }

    private void musicianRegistration() {

        boolean valid = false;

        System.out.println("--- Musician Registration ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();
        String gender = getValidGender();
        String email = getValidEmail();
        String password = getValidPassword();

        List<InstrumentBean> instruments = new ArrayList<>();
        
        System.out.print("-- Add your instruments (you must have at least one) --\n");

        boolean done = false;

        do{

            System.out.println("Instrument name: ");
            String instrumentName = scanner.nextLine().trim();

            while (instrumentName.isBlank()){
                System.out.println("Please enter the name of the instrument: ");
                instrumentName = scanner.nextLine().trim();
            }

            System.out.println("How good are you with "+instrumentName+"?");
            System.out.println("[1] Amateur");
            System.out.println("[2] Beginner");
            System.out.println("[3] Intermediate");
            System.out.println("[4] Experienced");
            System.out.println("[5] Master");
            System.out.println("> ");
            String masteryNumber = scanner.nextLine().trim();
            String mastery = "";

            valid = switch (masteryNumber) {
                case "1" -> {
                    mastery = "AMATEUR";
                    yield true;
                }
                case "2" -> {
                    mastery = "BEGINNER";
                    yield true;
                }
                case "3" -> {
                    mastery = "INTERMEDIATE";
                    yield true;
                }
                case "4" -> {
                    mastery = "EXPERIENCED";
                    yield true;
                }
                case "5" -> {
                    mastery = "MASTER";
                    yield true;
                }
                default -> {
                    showError("Invalid input. Please try again.");
                    yield false;
                }
            };

            
            if (valid){
                instruments.add(new InstrumentBean(instrumentName, mastery));
            }
            
            System.out.println("Do you want to add another instrument? [y/N]: ");
            String answer = scanner.nextLine().trim();
            
            if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("yes")) {
                done = true;
            }
            
            
        }while(!done);

        System.out.println("Stage name: ");
        String stageName = scanner.nextLine().trim();

        try {
            MusicianBean mb = new MusicianBean(name, surname, email, gender.toUpperCase(), password, stageName, instruments);
            SessionBean session = loginController.musicianRegistration(mb);
            showInfo("Registration successful! Welcome " + session.getMusician().getName());
            navigator.setCurrentScreen(Screen.MUSICIAN_DASHBOARD);
            navigator.nextScreen();
        } catch (ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
            start(scanner);
        }
    }

    private void promoterRegistration() {
        boolean valid = false;

        System.out.println("--- Promoter Registration ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();
        String gender = getValidGender();
        String email = getValidEmail();
        String password = getValidPassword();

        Map<String, String> contacts = new HashMap<>();

        boolean done = false;

        do{

            System.out.println("-- Insert contacts (at least one) --");
            System.out.print("Contact type (e.g. phone): ");
            String contactType = scanner.nextLine().trim();
            System.out.print("Contact value: ");
            String contactValue = scanner.nextLine().trim();
            contacts.put(contactType, contactValue);

            while (contactType.isBlank() || contactValue.isBlank()) {
                System.out.println("Invalid contact information. Please try again.");
                System.out.print("Contact type (e.g. phone): ");
                contactType = scanner.nextLine().trim();
                System.out.print("Contact value: ");
                contactValue = scanner.nextLine().trim();
            }

            System.out.println("Do you want to insert another contact? [y/N]: ");
            String answer = scanner.nextLine().trim();

            if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("yes")) {
                done = true;
            }

        }while(!done);


        try {
            PromoterBean pb = new PromoterBean(name, surname, email, gender.toUpperCase(), password, contacts);
            SessionBean session = loginController.promoterRegistration(pb);
            showInfo("Registration successful! Welcome " + session.getPromoter().getName());
            navigator.setCurrentScreen(Screen.PROMOTER_DASHBOARD);
            navigator.nextScreen();
        } catch (ControllerLogicException | IllegalArgumentException e) {
            showError(e.getMessage());
            start(scanner);
        }
    }

    @Override
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    @Override
    public void showInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    private String getValidEmail(){
        String email = "";
        boolean valid = false;

        while(!valid){
            System.out.print("Email: ");
            email = scanner.nextLine().trim();

            if(!EmailChecker.isValidEmail(email)){
                System.out.println("Invalid Email, please try again.");
            }
            else{
                valid = true;
            }

        }

        return email;
    }

    private String getValidPassword(){
        String password = "";
        boolean valid = false;

        while (!valid) {

            System.out.print("Password: ");

            password = scanner.nextLine().trim();

            valid = PasswordChecker.isPasswordValid(password);

            if (!valid){
                System.err.println("Password cannot contain these characters: "+PasswordChecker.getInvalidCharacters());
            }

        }

        return password;
    }

    private String getValidGender(){

        boolean valid = false;

        String gender = "";

        while(!valid) {

            System.out.print("Gender (MALE/FEMALE/UNSPECIFIED) [M/F/U]: ");
            gender = scanner.nextLine().trim();

            valid = switch (gender) {
                case "M" -> {
                    gender = "MALE";
                    yield true;
                }
                case "F" -> {
                    gender = "FEMALE";
                    yield true;
                }
                case "U" -> {
                    gender = "NOT_SPECIFIED";
                    yield true;
                }
                default -> {
                    showError("Invalid input. Please try again.");
                    yield false;
                }
            };
        }

        return gender;
    }

}