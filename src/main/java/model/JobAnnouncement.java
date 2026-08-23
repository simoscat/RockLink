package model;

import engineering.enums.JobAnnouncementStatus;

import java.time.LocalDateTime;

public interface JobAnnouncement {

    Promoter getPublisher();
    String getTitle();
    String getContent();
    LocalDateTime getAnnouncementDate();

    void openAnnouncement();

    MoneyValue getJobPay();

    void hireArtist(Artist artist);

    void closeAnnouncement();

    JobAnnouncementStatus getStatus();

    Artist whoWasHired();

    LocalDateTime getAnnouncementPublishDate();
    void publishNow();

    String getEventAddress();

    void setStatus(JobAnnouncementStatus status);
}
