package model;

import engineering.enums.AnnouncementType;
import engineering.enums.ApplicationStatus;

public abstract class Application {
    private Announcement announcementReference;
    private String candidateEmail;
    private ApplicationStatus status;
    private AnnouncementType type;

    protected Application(Announcement a, String email, AnnouncementType t, ApplicationStatus s) {
        this.announcementReference = a;
        this.candidateEmail = email;
        this.type = t;
        this.status = s;
    }

    public Announcement getAnnouncementReference() {
        return announcementReference;
    }

    //TODO
}
