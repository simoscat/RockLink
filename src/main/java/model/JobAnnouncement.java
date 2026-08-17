package model;


import engineering.enums.AnnouncementStatus;

import java.time.LocalDateTime;
import java.util.Map;

/*
for the time being, this is the only subclass of BaseAnnouncement; in the future there will be another subclass,
BandAnnouncement, to implement band recruitment, as specified in the documentation.
 */

public class JobAnnouncement extends BaseAnnouncement {

    private Promoter promoter;
    private MoneyValue salary;
    private String address;

    public JobAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status,
                              LocalDateTime publishDate, Artist hiredArtist, Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date, status, publishDate, hiredArtist);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
    }

    public JobAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status, LocalDateTime publishDate,
                           Promoter promoter,  MoneyValue salary, String address) {
        super(title, content, date, status, publishDate);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
    }

    public JobAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status,
                           Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date, status);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
    }


    @Override
    public User getPublisher() {
        return this.promoter;
    }

    public void giveJobToPromoter(Promoter promoter) {
        this.promoter = promoter;
    }

}
