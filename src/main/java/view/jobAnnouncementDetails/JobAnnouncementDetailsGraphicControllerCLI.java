package view.jobAnnouncementDetails;

import bean.JobAnnouncementBean;
import model.Artist;
import view.Navigator;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class JobAnnouncementDetailsGraphicControllerCLI extends JobAnnouncementDetailsGraphicController {

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    public JobAnnouncementDetailsGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        JobAnnouncementBean job = navigator.getCurrentJobAnnouncement();

        printAnnouncementDetails(job);

        if (isMusician()) {
            musicianMenu();
        } else {
            promoterMenu();
        }

    }

    private void printAnnouncementDetails(JobAnnouncementBean job) {

        System.out.println("────── Job Posting Details ──────");

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.printf("  %s%n", job.getTitle());
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.printf("Status:        %s%n", job.getJobAnnouncementStatus());
        System.out.printf("Address:       %s%n", job.getAddress());
        System.out.printf("Pay:           %s %s%n", job.getMoneyValue().getValue(), job.getMoneyValue().getCurrency());
        System.out.printf("Event date:    %s%n", job.getDate().format(DTF));
        System.out.printf("Published:     %s%n", job.getPublishDate() != null ? job.getPublishDate().format(DTF) : "-");


        Artist hiredArtist = job.getHiredArtist();
        if (hiredArtist != null) {
            System.out.printf("Hired artist:  %s (%s)%n", hiredArtist.getArtistName(), hiredArtist.getType());
        }

        System.out.println("──────────────────────────────────────────────────────────────────");
        System.out.println(job.getContent());
        System.out.println("──────────────────────────────────────────────────────────────────");


        System.out.printf("Promoter:      %s %s%n", job.getPromoter().getName(), job.getPromoter().getSurname());

        System.out.println("Promoter contacts: ");

        for (Map.Entry<String, String> contact : job.getPromoter().getContacts().entrySet()){
            System.out.printf("- %s: %s%n", contact.getKey(), contact.getValue());
        }

        System.out.println("────── END OF JOB POSTING ──────");
        System.out.println();

        if (isMusician() && hasMusicianAlreadyApplied()){

            String status = getMusicianApplicationStatus();

            System.out.println("───────── APPLICATION ──────────");
            System.out.println("Application status: "+status);

        }
    }

    private void musicianMenu(){

        boolean done = false;

        while(!done){

            System.out.println("Available operations: ");


            System.out.println("[1] Apply for this job");
            System.out.println("[2] Back to dashboard");
            System.out.print("> ");

            switch(scanner.nextLine().trim()){

                case "1":
                    applyForJob();
                    done = true;
                    break;

                case "2":
                    backToDashboard();
                    done = true;
                    break;

                default:
                    showError("Invalid operation");

            }

        }


    }

    private void applyForJob(){

        String current = navigator.getCurrentJobAnnouncement().getMoneyValue().getValue() + " "
                + navigator.getCurrentJobAnnouncement().getMoneyValue().getCurrency();

        System.out.print("Do you want to do a counter offer? Base offer is "+current+" [Y/n]: ");

        BigDecimal raiseOffer = new BigDecimal(0);

        String answer = scanner.nextLine().trim();

        if (!answer.equalsIgnoreCase("n") &&
                !answer.equalsIgnoreCase("no")){

            boolean done = false;

            while (!done){

                try {
                    System.out.print("Insert your raise offer (how much more you want): ");

                    raiseOffer = new BigDecimal(Integer.parseInt(scanner.nextLine().trim()));

                    if (raiseOffer.compareTo(BigDecimal.ZERO) < 0){
                        showError("Invalid value. Try again");
                    }

                    else done = true;

                } catch (NumberFormatException e) {
                    showError("Invalid number. Try again");
                }

            }

        }

        applyMusicianForJob(raiseOffer);

    }

    private void promoterMenu(){

        System.out.println("Available operations: ");

        System.out.println("[1] Close Job Posting");
        System.out.println("[2] View Job Applications");
        System.out.print("> ");

        boolean done = false;

        while (!done){

            switch(scanner.nextLine().trim()){

                case "1":
                    closeJobPosting();
                    done = true;
                    break;
                case "2":
                    viewJobApplications();
                    done = true;
                    break;
                default:
                    showError("Invalid operation");
            }
        }
    }

    @Override
    protected void showError(String message) {
        System.out.println("[ERROR]: " + message);
    }

    @Override
    protected void showInfo(String message) {
        System.out.println("[INFO]: " + message);
    }
}
