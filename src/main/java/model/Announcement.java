package model;

import engineering.enums.AnnouncementStatus;

import java.time.LocalDateTime;

public interface Announcement {

    public User getPublisher();
    public String getTitle();
    public String getContent();
    public LocalDateTime getAnnouncementDate();

    public void openAnnouncement();
    public void hireArtist(Artist artist);
    public void closeAnnouncement();

    public AnnouncementStatus getStatus();

    String getId();
}
