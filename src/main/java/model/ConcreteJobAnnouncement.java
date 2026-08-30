package model;


import engineering.enums.JobAnnouncementStatus;

import java.time.LocalDateTime;

public class ConcreteJobAnnouncement extends BaseAnnouncement implements JobAnnouncement {

    private Promoter promoter;
    private MoneyValue salary;
    private String address;
    private Artist hiredArtist = null;
    private JobAnnouncementStatus jobAnnouncementStatus;

    //7 args constructor to avoid sonar smell (status is not initialized)
    public ConcreteJobAnnouncement(String title, String content, LocalDateTime date, LocalDateTime publishDate,
                                   Promoter promoter, MoneyValue salary, String address) {
        super(title, content, date, publishDate);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
    }

    public ConcreteJobAnnouncement(String title, String content, LocalDateTime date,
                                   JobAnnouncementStatus status, Promoter promoter,
                                   MoneyValue salary, String address) {
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
    public void setStatus(JobAnnouncementStatus status) {
        this.jobAnnouncementStatus = status;
    }

    @Override
    public MoneyValue getJobPay() {
        return this.salary;
    }

    @Override
    public void hireArtist(Artist artist){
        this.hiredArtist = artist;
        if (artist != null) {
            this.jobAnnouncementStatus = JobAnnouncementStatus.FILLED;
        }
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
