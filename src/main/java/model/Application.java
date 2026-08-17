package model;

import engineering.enums.ApplicationStatus;

public class Application {

    private Announcement announcementReference;
    private String candidateEmail;
    private ApplicationStatus status;

    public Application(Announcement a, String email, ApplicationStatus s) {
        this.announcementReference = a;
        this.candidateEmail = email;
        this.status = s;
    }

    public Announcement getApplicationAnnouncement() {
        return announcementReference;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public ApplicationStatus currentApplicationStatus() {
        return status;
    }

    public void acceptApplication(){
        this.status = ApplicationStatus.ACCEPTED;
    }

    public void rejectApplication(){
        this.status = ApplicationStatus.REJECTED;
    }

}
