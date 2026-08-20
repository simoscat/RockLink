package view.createAnnouncement;

import bean.JobAnnouncementBean;
import bean.MoneyValueBean;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementTag;
import view.Navigator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CreateAnnouncementGraphicControllerCLI extends CreateAnnouncementGraphicController{

    Scanner scanner = new Scanner(System.in);

    public CreateAnnouncementGraphicControllerCLI(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void start() {

        printHeader();

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.println("Description (type quit to finish): ");

        StringBuilder description = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("quit")) {
                break;
            }
            description.append(line).append("\n");
        }

        String content = description.toString();

        LocalDateTime date = readDateAndTime();

        System.out.print("Event address (please include the city): ");
        String address = scanner.nextLine();

        MoneyValueBean moneyValue = readMoneyValue();

        List<String> tags = readTags();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean(
                title,
                content,
                date,
                navigator.getPromoter(),
                moneyValue,
                address,
                tags
        );


        publishAnnouncement(jobAnnouncementBean);

    }

    private List<String> readTags() {

        JobAnnouncementTag[] availableTags = JobAnnouncementTag.values();

        while (true) {

            System.out.println("Select tags (for more tags separate with space, e.g. 1 2 3)");
            System.out.println("If you don't want any tags, just press enter");

            System.out.println("Available tags:");
            for (int i = 0; i < availableTags.length; i++) {
                System.out.printf("[%d] %s%n", i + 1, availableTags[i]);
            }

            System.out.print("> ");

            String[] choices = scanner.nextLine().trim().split("\\s+");

            List<String> tags = new ArrayList<>();

            if (choices.length == 0 || (choices.length == 1 && choices[0].isEmpty())) {
                return tags;
            }

            boolean valid = true;

            for (String choice : choices) {

                try {
                    int index = Integer.parseInt(choice) - 1;

                    if (index < 0 || index >= availableTags.length) {
                        showError("Invalid tag: " + choice);
                        valid = false;
                        break;
                    }

                    String tagName = availableTags[index].name();
                    if (!tags.contains(tagName)) {
                        tags.add(tagName);
                    }

                } catch (NumberFormatException e) {
                    showError("Invalid tag: " + choice);
                    valid = false;
                    break;
                }

            }

            if (valid) {
                return tags;
            }

        }

    }

    private MoneyValueBean readMoneyValue() {

        while (true) {
            System.out.print("Pay amount: ");
            String money = scanner.nextLine();

            if (Float.parseFloat(money) <= 0) {
                showError("Invalid pay amount");
                continue;
            }

            CurrencyType[] currencies = CurrencyType.values();

            System.out.println("Select currency: ");

            for (int i = 0; i < currencies.length; i++) {

                System.out.printf("[%d] %s%n", i + 1, currencies[i]);

            }

            System.out.print("> ");
            int selection = Integer.parseInt(scanner.nextLine().trim()) - 1;

            String currency = CurrencyType.EUR.name();

            if (selection > 0 && selection < currencies.length) {
                currency = currencies[selection].name();
            }

            return new MoneyValueBean(
                    currency,
                    BigDecimal.valueOf(Float.parseFloat(money))
            );
        }

    }

    private LocalDateTime readDateAndTime() {

        while (true) {
            try {
                System.out.print("Date of the event [yyyy-MM-dd] (example 2023-03-01) : ");
                String date = scanner.nextLine().trim();

                System.out.print("Time of the event [HH:mm] (example 01:00) : ");
                String time = scanner.nextLine().trim();

                LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

                if (dateTime.isBefore(LocalDateTime.now())) {
                    showError("The date and time cannot be in the past");
                } else {
                    return dateTime;
                }

            } catch (DateTimeParseException e) {
                showError("Invalid date and time format");
            }
        }

    }


    private void printHeader(){

        System.out.println("─────────────── NEW JOB ANNOUNCEMENT ───────────────");

    }

    @Override
    protected void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    @Override
    protected void showInfo(String message) {
        System.out.println("[INFO] " + message);
    }


}
