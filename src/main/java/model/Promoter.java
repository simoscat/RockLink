package model;

import engineering.enums.Gender;

public class Promoter extends User {

    String role;
    JobEvent jobEvent;

    public Promoter(String name, String surname, String email, Gender gender, JobEvent jobEvent,
                    String role) {
        super(name, surname, email, gender);
        this.role = role;
        this.jobEvent = jobEvent;

        if (jobEvent != null) {
            this.jobEvent.addPromoter(this);
        }
    }

    public void changeLocal(JobEvent jobEvent){
        this.jobEvent.removePromoter(this);
        this.jobEvent = jobEvent;
        this.jobEvent.addPromoter(this);
    }


    public String getRole() {
        return this.role;
    }

    public JobEvent getJobEvent() {
        return this.jobEvent;
    }
}
