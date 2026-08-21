package view.openAnnouncementsDiscovery;

import bean.JobAnnouncementBean;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class OpenAnnouncementsDiscoveryGraphicControllerCLI extends OpenAnnouncementsDiscoveryGraphicController {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

    private static final int NUM_WIDTH = 3;
    private static final int DATE_WIDTH = 18;
    private static final int ADDRESS_WIDTH = 25;
    private static final int PAY_WIDTH = 14;
    private static final int STATUS_WIDTH = 10;

    private static final String TITLE_HEADER = "Title";

    private final Scanner scanner = new Scanner(System.in);


    public OpenAnnouncementsDiscoveryGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        printHeader();

        showOpenJobAnnouncements();

        showMenu();

    }

    private void printHeader() {

        System.out.println("╔═══════════════════════════════════════════╗\n");
        System.out.println("║              Available jobs               ║\n");
        System.out.println("╚═══════════════════════════════════════════╝\n");


    }

    private void showOpenJobAnnouncements() {

        List<JobAnnouncementBean> openJobAnnouncements = findOpenJobAnnouncements();


        if (openJobAnnouncements.isEmpty()) {
            System.out.println("No open job announcements found");
        } else {
            printOpenJobAnnouncementsTable(openJobAnnouncements);
        }

    }

    private void printOpenJobAnnouncementsTable(List<JobAnnouncementBean> openJobAnnouncements) {

        int titleWidth = calculateTitleWidth(openJobAnnouncements);

        String rowFormat = "%-" + NUM_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + titleWidth + "s | %-"
                + DATE_WIDTH + "s | %-" + ADDRESS_WIDTH + "s | %-" + PAY_WIDTH + "s | %-" + STATUS_WIDTH + "s%n";

        System.out.printf(rowFormat, "#", "Published", TITLE_HEADER, "Event date", "Address", "Pay", "Status");

        System.out.println(
                "-".repeat(NUM_WIDTH) + "-+-" +
                "-".repeat(DATE_WIDTH) + "-+-" +
                "-".repeat(titleWidth) + "-+-" +
                "-".repeat(DATE_WIDTH) + "-+-" +
                "-".repeat(ADDRESS_WIDTH) + "-+-" +
                "-".repeat(PAY_WIDTH) + "-+-" +
                "-".repeat(STATUS_WIDTH)
        );

        int number = 1;

        for (JobAnnouncementBean job : openJobAnnouncements) {

            String publishDate = job.getPublishDate() != null ? job.getPublishDate().format(DTF) : "-";
            String eventDate = job.getDate() != null ? job.getDate().format(DTF) : "-";
            String address = truncate(job.getAddress(), ADDRESS_WIDTH);
            String pay = job.getMoneyValue().getValue() + " " + job.getMoneyValue().getCurrency();

            System.out.printf(rowFormat,
                    number++,
                    publishDate,
                    job.getTitle(),
                    eventDate,
                    address,
                    truncate(pay, PAY_WIDTH),
                    job.getJobAnnouncementStatus()
            );

        }

        System.out.println();

    }

    private void showMenu() {

        boolean done = false;

        while (!done) {
            System.out.println("Available operations:");

            System.out.println("[1] Open a job announcement");
            System.out.println("[2] Filter: show ALL job announcements");
            System.out.println("[3] Filter: show OPEN job announcements");
            System.out.println("[4] Refresh");
            System.out.println("[5] Back to dashboard");
            System.out.print("> ");

            switch(scanner.nextLine().trim()){

                case "1":
                    openAJobAnnouncement();
                    done = true;
                    break;

                case "2":
                    allStart();
                    done = true;
                    break;

                case "3":
                    openStart();
                    done = true;
                    break;

                case "4":
                    refreshUI();
                    done = true;
                    break;

                case "5":
                    backToMusicianDashboard();
                    done = true;
                    break;

                default:
                    showError("Invalid choice.");

            }
        }

    }


    private void openAJobAnnouncement() {

        if (navigator.getJobAnnouncements().isEmpty()){
            System.out.println("No job announcements to open.");
            start();
        }

        boolean done = false;
        while (!done){

            System.out.print("Insert the number of the announcement to open: ");

            try{

                int num = Integer.parseInt(scanner.nextLine().trim());

                if (num <= 0 || num > navigator.getJobAnnouncements().size()){

                    showError("Invalid choice.");

                }

                navigator.setCurrentJobAnnouncement(navigator.getJobAnnouncements().get(num - 1));
                goToJobAnnouncement();
                done = true;

            }
            catch (NumberFormatException e){

                showError("Invalid number.");

            }

        }



    }

    private int calculateTitleWidth(List<JobAnnouncementBean> openJobAnnouncements) {

        int maxWidth = TITLE_HEADER.length();

        for (JobAnnouncementBean job : openJobAnnouncements) {
            if (job.getTitle() != null) {
                maxWidth = Math.max(maxWidth, job.getTitle().length());
            }
        }

        return maxWidth;

    }

    private String truncate(String value, int width) {

        if (value == null) {
            return "";
        }

        return value.length() > width ? value.substring(0, width - 1) + "…" : value;
    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void showInfo(String message) {

    }
}
