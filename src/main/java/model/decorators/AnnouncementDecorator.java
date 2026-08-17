package model.decorators;

import engineering.enums.AnnouncementStatus;
import model.Announcement;
import model.Artist;
import model.User;

import java.time.LocalDateTime;

public abstract class AnnouncementDecorator implements Announcement {

    private Announcement wrappedAnnouncement;

    protected AnnouncementDecorator(Announcement wrappedAnnouncement) {
        this.wrappedAnnouncement = wrappedAnnouncement;
    }

    public Announcement unwrapAnnouncement() {
        return wrappedAnnouncement;
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
    public LocalDateTime getAnnouncementDate() {
        return this.wrappedAnnouncement.getAnnouncementDate();
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

    @Override
    public String getId() {
        return this.wrappedAnnouncement.getId();
    }
}
