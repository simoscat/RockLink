package view.promoterDashboard;

import bean.JobAnnouncementBean;
import model.JobAnnouncement;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PromoterDashboardGraphicControllerCLI extends PromoterDashboardGraphicController{

    private Scanner scanner = new Scanner(System.in);

    public PromoterDashboardGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {
        printHeader();

        List<JobAnnouncementBean> jobAnnouncements = showAnnouncements();
        showMenu(jobAnnouncements);

    }

    private List<JobAnnouncementBean> showAnnouncements(){

        System.out.println("─────────────── PUBLISHED JOB ANNOUNCEMENTS ───────────────");

        List<JobAnnouncementBean> jobAnnouncements = getPromoterJobAnnouncements();

        if (jobAnnouncements == null || jobAnnouncements.isEmpty()) {
            System.out.println("You haven't published any job announcements.");
            return jobAnnouncements;
        }

        System.out.printf("%-5s %-17s %-40s %-17s %-6s %-9s%n",
                "#", "Pub. date", "Title", "Event date", "Status", "Pay");
        System.out.println("───────────────────────────────────────────────────────" +
                "────────────────────────────────────────────────────");

        for (int i = 0; i < jobAnnouncements.size(); i++) {

            JobAnnouncementBean announcement = jobAnnouncements.get(i);

            String pay = announcement.getMoneyValue().getValue() + " " + announcement.getMoneyValue().getCurrency();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

            System.out.printf("[%-3d] %-17s %-40s %-17s %-6s %-9s%n",
                    i + 1,
                    announcement.getPublishDate().format(dtf),
                    announcement.getTitle().substring(0, Math.min(announcement.getTitle().length(), 40)),
                    announcement.getDate().format(dtf),
                    announcement.getJobAnnouncementStatus(),
                    pay);
        }

        System.out.println("────────────────────────────────────────────────────────────────────────────" +
                "───────────────────────────────");


        return jobAnnouncements;
    }

    private void printHeader() {
        System.out.println("╔══════════════════════════════════╗\n");
        System.out.println("║        Dashboard Promoter        ║\n");
        System.out.println("╚══════════════════════════════════╝\n");

        System.out.printf("Promoter %s %s%n", navigator.getSession().getPromoter().getName(),
                navigator.getSession().getPromoter().getSurname());
        System.out.println("Email: "+navigator.getSession().getPromoter().getEmail());
    }

    private void showMenu(List<JobAnnouncementBean> jobAnnouncements) {

        System.out.println("Choose an option: ");
        System.out.println("[0] Publish a new job announcement");
        System.out.println("[N] Open Job Announcement (type announcement number) ");
        System.out.println("[X] Logout");
        System.out.print("> ");

        String operation = scanner.nextLine().trim();

        int announcementNumber = jobAnnouncements.toArray().length;

        if (operation.equalsIgnoreCase("X")) {
            doLogout();
        }
        else if (operation.equalsIgnoreCase("R")) {
            reloadDashboard();
        }

        else {
            try {
                int opNumber = Integer.parseInt(operation);

                if (opNumber == 0) {

                    goToCreateAnnouncement();

                } else if (opNumber > 0 && opNumber <= announcementNumber) {

                    goToJobAnnouncement(jobAnnouncements.get(opNumber - 1));

                } else {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                showError("Please enter a valid character or a valid numeric value >= 0");
                start();
            }
        }

    }

    @Override
    public void showError(String message) {
        System.out.println("[ERROR] "+message);
    }

    @Override
    public void showInfo(String message) {
        System.out.println("[INFO] "+message);
    }
}
