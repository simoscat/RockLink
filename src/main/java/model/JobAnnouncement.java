package model;

import engineering.enums.JobAnnouncementStatus;

import java.time.LocalDateTime;

public interface JobAnnouncement {

    public Promoter getPublisher();
    public String getTitle();
    public String getContent();
    public LocalDateTime getAnnouncementDate();

    public void openAnnouncement();

    MoneyValue getJobPay();

    public void hireArtist(Artist artist);
    public void closeAnnouncement();

    public JobAnnouncementStatus getStatus();

    public Artist whoWasHired();

    public LocalDateTime getAnnouncementPublishDate();
    public void publishNow();

    public String getEventAddress();
}
