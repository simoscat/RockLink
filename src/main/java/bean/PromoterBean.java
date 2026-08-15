package bean;

import model.JobEvent;

public class PromoterBean extends UserBean{

    private String role;
    private JobEvent jobEvent;

    protected PromoterBean(String name, String surname, String email, String gender, String password,
                           String role, JobEvent jobEvent) {
        super(name, surname, email, gender, password);
        this.role = role;
        this.jobEvent = jobEvent;
    }

    protected PromoterBean(String email, String password) {
        super(email, password);
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public JobEvent getJobEvent() {
        return jobEvent;
    }
    public void setJobEvent(JobEvent jobEvent) {
        this.jobEvent = jobEvent;
    }
    //TODO
}
