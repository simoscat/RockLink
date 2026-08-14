package model;


public class JobAnnouncement extends Announcement{
    private Promoter promoter;
    private MoneyValue salary;

    public JobAnnouncement(String id, String title, String content, String date, AnnouncementStatus status,
                           Promoter promoter, MoneyValue salary){
        super(id, title, content, date, status);
        this.promoter = promoter;
        this.salary = salary;
    }

    //TODO
}
