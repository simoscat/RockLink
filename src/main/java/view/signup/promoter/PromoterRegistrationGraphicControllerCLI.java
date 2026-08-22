package view.signup.promoter;

import view.Navigator;

import java.util.HashMap;
import java.util.Scanner;

public class PromoterRegistrationGraphicControllerCLI extends PromoterRegistrationGraphicController {

    private final Scanner scanner = new Scanner(System.in);

    public PromoterRegistrationGraphicControllerCLI(Navigator n) {
        super(n);
    }

    @Override
    public void start() {

        System.out.println("--- Promoter Registration ---");
        System.out.print("Name: ");
        this.name = scanner.nextLine();//we don't trim cause multiple names
        System.out.print("Surname: ");
        this.surname = scanner.nextLine().trim();
        this.gender = getGender();
        System.out.print("Email: ");
        this.email = scanner.nextLine().trim();
        System.out.print("Password: ");
        this.password = scanner.nextLine();

        this.contacts = new HashMap<>();

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

            System.out.print("Do you want to insert another contact? [y/N]: ");
            String answer = scanner.nextLine().trim();

            if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("yes")) {
                done = true;
            }

        }while(!done);

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

        while (true) {

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
                    showError("Invalid selection");

            }

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

}
