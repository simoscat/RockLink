package model.jobAnnouncementDecorators;

import engineering.enums.JobAnnouncementStatus;
import model.JobAnnouncement;
import model.Artist;
import model.MoneyValue;
import model.User;

import java.time.LocalDateTime;

public abstract class JobAnnouncementDecorator implements JobAnnouncement {

    private JobAnnouncement wrappedJobAnnouncement;

    protected JobAnnouncementDecorator(JobAnnouncement wrappedJobAnnouncement) {
        this.wrappedJobAnnouncement = wrappedJobAnnouncement;
    }

    public JobAnnouncement unwrapAnnouncement() {
        return wrappedJobAnnouncement;
    }

    @Override
    public User getPublisher() {
        return this.wrappedJobAnnouncement.getPublisher();
    }

    @Override
    public String getTitle() {
        return this.wrappedJobAnnouncement.getTitle();
    }

    @Override
    public String getContent() {
        return this.wrappedJobAnnouncement.getContent();
    }

    @Override
    public LocalDateTime getAnnouncementDate() {
        return this.wrappedJobAnnouncement.getAnnouncementDate();
    }

    @Override
    public void openAnnouncement() {
        this.wrappedJobAnnouncement.openAnnouncement();
    }

    @Override
    public MoneyValue getJobPay() {
        return this.wrappedJobAnnouncement.getJobPay();
    }

    @Override
    public void hireArtist(Artist artist) {
        this.wrappedJobAnnouncement.hireArtist(artist);
    }

    @Override
    public void closeAnnouncement() {
        this.wrappedJobAnnouncement.closeAnnouncement();
    }


    @Override
    public JobAnnouncementStatus getStatus() {
        return this.wrappedJobAnnouncement.getStatus();
    }

    @Override
    public Artist whoWasHired() {
        return this.wrappedJobAnnouncement.whoWasHired();
    }

    @Override
    public LocalDateTime getAnnouncementPublishDate() {
        return this.wrappedJobAnnouncement.getAnnouncementPublishDate();
    }

    @Override
    public void publishNow() {
        this.wrappedJobAnnouncement.publishNow();
    }

    @Override
    public String getEventAddress() {
        return this.wrappedJobAnnouncement.getEventAddress();
    }


}
