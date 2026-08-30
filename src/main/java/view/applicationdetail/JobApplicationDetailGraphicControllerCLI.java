package view.applicationdetail;

import view.Navigator;

import java.util.Map;
import java.util.Scanner;

public class JobApplicationDetailGraphicControllerCLI extends JobApplicationDetailGraphicController {

    private Scanner scanner = new Scanner(System.in);

    public JobApplicationDetailGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        printApplication();

        showMenu();

    }

    private void printApplication() {

        System.out.println("Applicant Details");

        Map<String, String> details = getCurrentJobApplication().getArtist().getDetails();

        for (Map.Entry<String, String> entry : details.entrySet()) {

            System.out.println("- " + entry.getKey() + ": " + entry.getValue());

        }

    }

    private void showMenu() {

        System.out.println("Available operations:");
        System.out.println("[1] Accept application");
        System.out.println("[2] Reject application");
        System.out.println("[3] Back to job applications");
        System.out.print("> ");

        switch(scanner.nextLine().trim()) {

            case "1":
                acceptApplication();
                break;
            case "2":
                rejectApplication();
                break;
            case "3":
                backToJobApplications();
                break;
            default:
                navigator.showError("Invalid input. Try again");
                start();

        }

    }

}
