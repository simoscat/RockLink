package model;

import engineering.enums.ApplicationStatus;

import java.math.BigDecimal;

public class JobApplication extends Application {

    private BigDecimal raiseOffer;

    //the raiseOffer models how much more the applicant wants on their salary

    /*
    example: announcement pays 200 USD but Musician wants 240 -> raiseOffer = 40

    if the salary is accepted as it is the counteroffer will be 0
     */

    public JobApplication(String announcementId, String email, ApplicationStatus s) {
        super(announcementId, email, s);
    }

    public JobApplication(String announcementId, String email, ApplicationStatus s, BigDecimal raiseOffer) {
        super(announcementId, email, s);
        this.raiseOffer = raiseOffer;
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


}
