package view.announcementApplications;

import bean.JobApplicationBean;
import controller.ManageJobApplicationsController;
import model.Artist;
import view.Navigator;

import java.util.List;
import java.util.Scanner;

public class AnnouncementApplicationsGraphicControllerCLI extends AnnouncementApplicationsGraphicController{

    private static final int EMAIL_WIDTH = 28;
    private static final int ARTIST_WIDTH = 22;
    private static final int OFFER_WIDTH = 15;
    private static final int NUM_WIDTH = 3;
    private static final int STATUS_WIDTH = 7;

    private static final int DETAILS = 1;
    private static final int ACCEPT = 2;
    private static final int REJECT = 3;

    private final Scanner scanner = new Scanner(System.in);

    public AnnouncementApplicationsGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }


    @Override
    public void start() {

        printHeader();

        List<JobApplicationBean> jobApplications = getJobApplications();

        if (jobApplications.isEmpty()) {
            System.out.println("No job applications found");
        }
        else{
            printApplicationsTable(jobApplications);
        }

        showMenu();

    }

    private void showMenu() {

        boolean done = false;

        while (!done) {
            System.out.println("Available operations:");
            System.out.println("[1] View applicant details");
            System.out.println("[2] Accept an application");
            System.out.println("[3] Reject an application");
            System.out.println("[4] Refresh applications");
            System.out.println("[5] Back to job announcement");
            System.out.print("> ");

            switch (scanner.nextLine().trim()) {

                case "1":
                    selectAnApplication(DETAILS);
                    done = true;
                    break;

                case "2":
                    selectAnApplication(ACCEPT);
                    done = true;
                    break;

                case "3":
                    selectAnApplication(REJECT);
                    done = true;
                    break;

                case "4":
                    refreshUI();
                    done = true;
                    break;

                case "5":
                    backToJobAnnouncement();
                    done = true;
                    break;

                default:
                    showError("Invalid input");
            }
        }

    }


    private void selectAnApplication(int in){

        try{
            System.out.print("Enter application number: ");
            int num = Integer.parseInt(scanner.nextLine().trim());

            if (num <= 0 || num > navigator.getJobApplications().size()){
                showError("Number is out of range. Try again");
                start();
            }
            else{

                navigator.setCurrentJobApplication(navigator.getJobApplications().get(num - 1));

                switch (in){

                    case DETAILS:
                        jumpToJobApplication();

                    case ACCEPT:
                        acceptJobApplication();

                    case REJECT:
                        rejectJobApplication();

                    default:
                        throw new IllegalStateException("Unexpected value: " + in);

                }

            }

        }
        catch (NumberFormatException e){

            showError("Invalid input. Try again.");
            start();

        }

    }

    private void printApplicationsTable(List<JobApplicationBean> jobApplications) {

        String rowFormat = "%-"+NUM_WIDTH+"s | %-" + EMAIL_WIDTH + "s | %-" + ARTIST_WIDTH + "s | %-" +
                OFFER_WIDTH + "s | %-" + STATUS_WIDTH + "s%n";

        System.out.printf(rowFormat, "#", "Email", "Artist", "Counteroffer", "Status");
        System.out.println("-".repeat(NUM_WIDTH) + "-+-" + "-".repeat(EMAIL_WIDTH) + "-+-" +
                "-".repeat(ARTIST_WIDTH) + "-+-" + "-".repeat(OFFER_WIDTH) + "-+-" + "-".repeat(STATUS_WIDTH));

        for (int i = 0; i < jobApplications.size(); i++) {

            JobApplicationBean jobApplication = jobApplications.get(i);
            Artist artist = jobApplication.getArtist();

            String email = truncate(artist.getEmail(), EMAIL_WIDTH);
            String artistName = truncate(artist.getArtistName(), ARTIST_WIDTH);
            String counterOffer = jobApplication.getRaiseOffer() != null
                    ? "+" + jobApplication.getRaiseOffer()
                    : "-";

            System.out.printf(rowFormat, i + 1, email, artistName, truncate(counterOffer, OFFER_WIDTH),
                    jobApplication.getStatus());
        }

        System.out.println("-".repeat(NUM_WIDTH + EMAIL_WIDTH + ARTIST_WIDTH + OFFER_WIDTH + STATUS_WIDTH + 12));
        System.out.println();

    }

    private String truncate(String value, int width) {

        if (value == null) {
            return "";
        }

        return value.length() > width ? value.substring(0, width - 1) + "…" : value;
    }

    private void printHeader(){
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("             Applications for current job announcement               ");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    @Override
    protected void showError(String message) {

        System.out.println("[Error] " + message);

    }

    @Override
    protected void showInfo(String message) {

        System.out.println("[Info] " + message);

    }
}
