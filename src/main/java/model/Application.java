package model;

import engineering.enums.ApplicationStatus;

public abstract class Application {

    private Artist artist;
    private ApplicationStatus status;

    public Application(Artist artist, ApplicationStatus s) {
        this.artist = artist;
        this.status = s;
    }

    public Application(Artist artist){
        this.status = ApplicationStatus.PENDING;
        this.artist = artist;
    }

    public Artist whoIsCandidate() {
        return artist;
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
