package model;

import engineering.enums.ApplicationStatus;

import java.math.BigDecimal;

public class JobApplication extends Application {

    private BigDecimal raiseOffer;
    private JobAnnouncement jobAnnouncementReference;

    //the raiseOffer models how much more the applicant wants on their salary

    /*
    example: announcement pays 200 USD but Musician wants 240 -> raiseOffer = 40

    if the salary is accepted as it is the counteroffer will be 0
     */

    public JobApplication(JobAnnouncement jobAnnouncement, Artist artist) {
        super(artist);
        this.jobAnnouncementReference = jobAnnouncement;
        this.raiseOffer = BigDecimal.ZERO;
    }

    public JobApplication(JobAnnouncement jobAnnouncementReference, Artist artist, BigDecimal raiseOffer) {
        super(artist);
        this.raiseOffer = raiseOffer;
        this.jobAnnouncementReference = jobAnnouncementReference;
    }

    public JobApplication(JobAnnouncement jobAnnouncementReference, Artist artist, ApplicationStatus s, BigDecimal raiseOffer) {
        super(artist, s);
        this.raiseOffer = raiseOffer;
        this.jobAnnouncementReference = jobAnnouncementReference;
    }

    public BigDecimal currentRaiseAmount() {
        return this.raiseOffer;
    }

    public void resetRaise(){
        this.raiseOffer = BigDecimal.ZERO;
    }

    public void askForRaise(BigDecimal amount){
        this.raiseOffer = amount;
    }

    public JobAnnouncement whichJobAnnouncement(){
        return this.jobAnnouncementReference;
    }


}
