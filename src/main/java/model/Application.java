package model;

import engineering.enums.ApplicationStatus;

public abstract class Application {

    private String announcementId;
    private String candidateEmail;
    private ApplicationStatus status;

    public Application(String announcementId, String email, ApplicationStatus s) {
        this.announcementId = announcementId;
        this.candidateEmail = email;
        this.status = s;
    }

    public String getApplicationAnnouncementId() {
        return announcementId;
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
