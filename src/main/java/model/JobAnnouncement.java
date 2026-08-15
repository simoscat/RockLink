package model;


import engineering.enums.AnnouncementStatus;

import java.util.Date;

public class JobAnnouncement extends Announcement{
    private Promoter promoter;
    private MoneyValue salary;

    public JobAnnouncement(String id, String title, String content, Date date, AnnouncementStatus status,
                           Promoter promoter, MoneyValue salary){
        super(id, title, content, date, status);
        this.promoter = promoter;
        this.salary = salary;
    }

    //TODO
}
