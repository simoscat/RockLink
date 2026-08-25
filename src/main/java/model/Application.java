package model;

import engineering.enums.ApplicationStatus;

public abstract class Application {

    private Artist artist;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    //when application is instantiated for the first time it's considered pending
    //this removes the controller responsibility to initialize the value of the status

    protected Application(Artist artist, ApplicationStatus s) {
        this.artist = artist;
        this.status = s;
    }

    protected Application(Artist artist){
        this.artist = artist;
    }

    public Artist whoIsCandidate() {
        return artist;
    }

    public ApplicationStatus currentApplicationStatus() {
        return status;
    }

    public void accept(){
        this.status = ApplicationStatus.ACCEPTED;
    }

    public void reject(){
        this.status = ApplicationStatus.REJECTED;
    }

}
