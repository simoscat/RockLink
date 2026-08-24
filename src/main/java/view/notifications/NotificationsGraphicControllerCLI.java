package view.notifications;

import bean.NotificationBean;
import view.Navigator;

import java.util.List;
import java.util.Scanner;

public class NotificationsGraphicControllerCLI extends NotificationsGraphicController{

    private static final int NUM_WIDTH = 3;
    private static final int SENDER_WIDTH = 28;
    private static final int TIME_WIDTH = 18;
    private static final int BODY_WIDTH = 60;

    private static final String TABLE_SEP = "s | %-";

    private final Scanner scanner = new Scanner(System.in);

    public NotificationsGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        printHeader();

        showNotifications();

        showMenu();

    }

    private void showNotifications() {

        List<NotificationBean> notifications = getNotifications();

        if (notifications.isEmpty()) {

            System.out.println("No notifications found");
            return;

        }

        String rowFormat = "%-" + NUM_WIDTH + TABLE_SEP + SENDER_WIDTH + TABLE_SEP + TIME_WIDTH + TABLE_SEP +
                BODY_WIDTH + "s%n";

        System.out.printf(rowFormat, "#", "Sender", "Time", "Body");
        System.out.println("-".repeat(NUM_WIDTH) + "-+-" + "-".repeat(SENDER_WIDTH) + "-+-" +
                "-".repeat(TIME_WIDTH) + "-+-" + "-".repeat(BODY_WIDTH));

        for (int i = 0; i < notifications.size(); i++) {

            NotificationBean notification = notifications.get(i);

            System.out.printf(rowFormat,
                    i + 1,
                    truncate(notification.getSender(), SENDER_WIDTH),
                    notification.getTime().format(DTF),
                    truncate(buildBody(notification), BODY_WIDTH));

        }

        System.out.println("-".repeat(NUM_WIDTH + SENDER_WIDTH + TIME_WIDTH + BODY_WIDTH + 9));
        System.out.println();

    }

    private String truncate(String value, int width) {

        if (value == null) {
            return "";
        }

        return value.length() > width ? value.substring(0, width - 1) + "…" : value;
    }

    private void showMenu() {

        System.out.println("Available operations:");
        System.out.println("[1] Open notification job");
        System.out.println("[2] Refresh notifications");
        System.out.println("[3] Back to dashboard");
        System.out.print("> ");

        boolean done = false;

        while (!done){

            String operation = scanner.nextLine().trim();

            switch (operation) {
                case "1":
                    openNotificationJob();
                    done = true;
                    break;

                case "2":
                    refreshUI();
                    done = true;
                    break;

                case "3":
                    backToDashboard();
                    done = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        }

    }

    private void openNotificationJob() {

        System.out.print("Select notification number: ");

        boolean done = false;

        while (!done){

            try{

                int op = Integer.parseInt(scanner.nextLine().trim()) - 1;

                if (op < 0 || op >= navigator.getNotifications().size()){
                    System.out.println("Invalid choice.");
                }
                else{
                    navigator.setCurrentJobAnnouncement(navigator.getNotifications().get(op).getJobAnnouncement());
                    goToJobAnnouncement();
                    done = true;
                }

            }
            catch (NumberFormatException _){
                System.out.println("Invalid number.");
            }

        }

    }

    private void printHeader() {

        System.out.println("╔══════════════════════════════════╗\n");
        System.out.println("║           Notifications          ║\n");
        System.out.println("╚══════════════════════════════════╝\n");

    }


}
