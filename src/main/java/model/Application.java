package model;

import engineering.enums.ApplicationStatus;

public abstract class Application {
    private Announcement announcementReference;
    private String candidateEmail;
    private ApplicationStatus status;

    protected Application(Announcement a, String email, ApplicationStatus s) {
        this.announcementReference = a;
        this.candidateEmail = email;
        this.status = s;
    }

    public Announcement getAnnouncementReference() {
        return announcementReference;
    }

    //TODO
}
