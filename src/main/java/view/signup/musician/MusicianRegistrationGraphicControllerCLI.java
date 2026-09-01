package view.signup.musician;

import bean.InstrumentBean;
import model.Mastery;
import view.Navigator;

import java.util.*;

public class MusicianRegistrationGraphicControllerCLI extends MusicianRegistrationGraphicController {

    private final Scanner scanner = new Scanner(System.in);

    public MusicianRegistrationGraphicControllerCLI(Navigator n) {
        super(n);
    }

    @Override
    public void start() {


        System.out.println("--- Musician Registration ---");
        System.out.print("Name: ");
        this.name = scanner.nextLine();
        System.out.print("Surname: ");
        this.surname = scanner.nextLine();
        this.gender = getGender();
        System.out.print("Email: ");
        this.email = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();

        System.out.print("Stage name: ");
        this.stageName = scanner.nextLine();

        this.instruments = new ArrayList<>();

        System.out.print("-- Add your instruments (you must have at least one) --\n");

        boolean done = false;

        while(!done) {

            boolean valid = false;

            while(!valid) {

                System.out.print("Instrument name: ");
                String instrumentName = scanner.nextLine();

                while (instrumentName.isBlank()) {
                    System.out.println("Please enter the name of the instrument: ");
                    instrumentName = scanner.nextLine();
                }

                System.out.println("How good are you with " + instrumentName + "?");
                System.out.println("[1] Amateur");
                System.out.println("[2] Beginner");
                System.out.println("[3] Intermediate");
                System.out.println("[4] Experienced");
                System.out.println("[5] Master");
                System.out.print("> ");

                String masteryNumber = scanner.nextLine().trim();
                String mastery = "";

                valid = switch (masteryNumber) {
                    case "1" -> {
                        mastery = Mastery.AMATEUR.name();
                        yield true;
                    }
                    case "2" -> {
                        mastery = Mastery.BEGINNER.name();
                        yield true;
                    }
                    case "3" -> {
                        mastery = Mastery.INTERMEDIATE.name();
                        yield true;
                    }
                    case "4" -> {
                        mastery = Mastery.EXPERIENCED.name();
                        yield true;
                    }
                    case "5" -> {
                        mastery = Mastery.MASTER.name();
                        yield true;
                    }
                    default -> {
                        navigator.showError("Invalid input. Please try again.");
                        yield false;
                    }
                };

                if (valid) {
                    this.instruments.add(new InstrumentBean(instrumentName, mastery));
                }

            }

            System.out.print("Do you want to add another instrument? [y/N]: ");

            String answer = scanner.nextLine().trim();

            if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("yes")) {
                done = true;
            }

        }

        System.out.print("Is this okay? [Y/n]: ");

        String answer = scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
            System.out.println("Registration cancelled.");
            navigator.goToLogin();
        }
        else {
            doRegistration();
        }

    }

    private String getGender() {

        while (true){

            System.out.println("Select your gender: ");
            System.out.println("[1] Male");
            System.out.println("[2] Female");
            System.out.println("[3] Not specified");
            System.out.print("> ");

            switch(scanner.nextLine().trim()) {

                case "1":
                    return "MALE";
                case "2":
                    return "FEMALE";
                case "3":
                    return "NOT_SPECIFIED";

                default:
                    navigator.showError("Invalid selection");

            }

        }


    }



}