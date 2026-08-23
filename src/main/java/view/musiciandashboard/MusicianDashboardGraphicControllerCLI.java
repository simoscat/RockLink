package view.musiciandashboard;

import bean.JobAnnouncementBean;
import bean.JobApplicationBean;
import view.Navigator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MusicianDashboardGraphicControllerCLI extends MusicianDashboardGraphicController {

    private final Scanner scanner = new Scanner(System.in);

    public MusicianDashboardGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    private void printHeader() {
        System.out.println("╔══════════════════════════════════╗\n");
        System.out.println("║        Dashboard Musician        ║\n");
        System.out.println("╚══════════════════════════════════╝\n");

        System.out.printf("Musician %s %s [%s] %n", navigator.getSession().getMusician().getName(),
                navigator.getSession().getMusician().getSurname(), navigator.getSession().getMusician().getStageName());
        System.out.println("Email: "+navigator.getSession().getMusician().getEmail());
    }

    @Override
    public void start() {

        printHeader();

        printJobApplications();

        showMenu();

    }

    private void showMenu(){

        while(true){

            System.out.println("Available operations:");

            System.out.println("[1] View job announcement details");
            System.out.println("[2] View open job announcements");
            System.out.println("[3] Refresh dashboard");
            System.out.println("[4] Logout");
            System.out.print("> ");

            switch(scanner.nextLine().trim()){

                case "1":
                    viewJobDetails();
                    break;

                case "2":
                    viewOpenAnnouncements();
                    break;

                case "3":
                    refreshDashboard();
                    break;

                case "4":
                    logout();
                    return;

                default:
                    navigator.showError("Invalid selection. Please retry.");
                    break;

            }

        }

    }

    private void viewJobDetails(){

        boolean done = false;

        while (!done){

            try{
                System.out.print("Select an application number to view the announcement of: ");

                int num = Integer.parseInt(scanner.nextLine().trim());

                List<JobApplicationBean> applications = navigator.getJobApplications();

                if (num <= 0 || num > applications.size()){

                    navigator.showError("Invalid selection. Please retry.");
                    continue;

                }

                navigator.setCurrentJobAnnouncement(applications.get(num - 1).getJobAnnouncementReference());
                done = true;

            }
            catch (NumberFormatException _) {

                navigator.showError("Invalid number. Please retry.");

            }

        }

        goToJobAnnouncement();

    }

    private void printJobApplications(){

        System.out.println("─────────────── JOB APPLICATIONS ───────────────");

        List<JobApplicationBean> jobApplications = getApplications();

        if (jobApplications == null || jobApplications.isEmpty()) {
            System.out.println("You haven't submitted any job applications.");
            return;
        }

        System.out.printf("%-5s %-40s %-20s %-20s %-12s %-12s%n",
                "#", "Title", "Event date", "Pay + Raise offer", "Job Status", "App. status");
        System.out.println("──────────────────────────────────────────────────────────" +
                "────────────────────────────────────────────────────");

        for (int i = 0; i < jobApplications.size(); i++) {

            JobApplicationBean application = jobApplications.get(i);
            JobAnnouncementBean announcement = application.getJobAnnouncementReference();

            String pay = announcement.getMoneyValue().getValue().toString();

            String raise = application.getRaiseOffer().toString() + " " + announcement.getMoneyValue().getCurrency();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

            System.out.printf("[%-3d] %-40s %-20s %-20s %-12s %-12s%n",
                    i + 1,
                    application.getJobAnnouncementReference().getTitle().
                            substring(0, Math.min(application.getJobAnnouncementReference().getTitle()
                                    .length(), 40)),
                    application.getJobAnnouncementReference().getDate().format(dtf),
                    pay + " + " + raise,
                    application.getJobAnnouncementReference().getJobAnnouncementStatus(),
                    application.getStatus()
            );
        }

        System.out.println("──────────────────────────────────────────────────────────" +
                "────────────────────────────────────────────────────");
        System.out.println();

    }
}
