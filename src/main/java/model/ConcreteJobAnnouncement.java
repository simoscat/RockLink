package model;


import engineering.enums.JobAnnouncementStatus;

import java.time.LocalDateTime;

public class ConcreteJobAnnouncement extends BaseAnnouncement implements JobAnnouncement {

    private Promoter promoter;
    private MoneyValue salary;
    private String address;
    private Artist hiredArtist = null;
    private JobAnnouncementStatus jobAnnouncementStatus;

    public ConcreteJobAnnouncement(String title, String content, LocalDateTime date,
                                   LocalDateTime publishDate, Artist hiredArtist, Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date, publishDate);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
        this.hireArtist(hiredArtist);
        //we don't initialize status here because if someone is hired the status is automatically "filled"
    }

    public ConcreteJobAnnouncement(String title, String content, LocalDateTime date, JobAnnouncementStatus status, LocalDateTime publishDate,
                                   Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date, publishDate);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
        this.jobAnnouncementStatus = status;
    }

    public ConcreteJobAnnouncement(String title, String content, LocalDateTime date, JobAnnouncementStatus status,
                                   Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
        this.jobAnnouncementStatus = status;
    }


    @Override
    public Promoter getPublisher() {
        return this.promoter;
    }


    @Override
    public String getEventAddress() {
        return this.address;
    }

    @Override
    public MoneyValue getJobPay() {
        return this.salary;
    }

    @Override
    public void hireArtist(Artist artist){
        this.hiredArtist = artist;
        this.jobAnnouncementStatus = JobAnnouncementStatus.FILLED;
    }

    @Override
    public Artist whoWasHired(){
        return this.hiredArtist;
    }

    @Override
    public void openAnnouncement(){
        if (this.hiredArtist == null) {
            this.jobAnnouncementStatus = JobAnnouncementStatus.OPEN;
        }
    }

    @Override
    public void closeAnnouncement(){
        if (this.hiredArtist == null) {
            this.jobAnnouncementStatus = JobAnnouncementStatus.CLOSED;
        }
    }

    @Override
    public JobAnnouncementStatus getStatus(){
        return this.jobAnnouncementStatus;
    }

}
