package model;

import engineering.enums.AnnouncementStatus;

import java.util.Date;

public interface Announcement {

    public User getPublisher();
    public String getTitle();
    public String getContent();
    public Date getDate();

    public void openAnnouncement();
    public void hireArtist(Artist artist);
    public void closeAnnouncement();

    public AnnouncementStatus getStatus();
}
