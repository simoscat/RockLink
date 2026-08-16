package model;

import engineering.enums.AnnouncementStatus;

import java.util.Date;

public abstract class AnnouncementDecorator implements Announcement {

    private Announcement wrappedAnnouncement;

    protected AnnouncementDecorator(Announcement wrappedAnnouncement) {
        this.wrappedAnnouncement = wrappedAnnouncement;
    }

    @Override
    public User getPublisher() {
        return this.wrappedAnnouncement.getPublisher();
    }

    @Override
    public String getTitle() {
        return this.wrappedAnnouncement.getTitle();
    }

    @Override
    public String getContent() {
        return this.wrappedAnnouncement.getContent();
    }

    @Override
    public Date getDate() {
        return this.wrappedAnnouncement.getDate();
    }

    @Override
    public void openAnnouncement() {
        this.wrappedAnnouncement.openAnnouncement();
    }

    @Override
    public void hireArtist(Artist artist) {
        this.wrappedAnnouncement.hireArtist(artist);
    }

    @Override
    public void closeAnnouncement() {
        this.wrappedAnnouncement.closeAnnouncement();
    }


    @Override
    public AnnouncementStatus getStatus() {
        return this.wrappedAnnouncement.getStatus();
    }
}
