package bean;

import engineering.EmailChecker;
//TODO CHECK
public class ApplicationBean {

    private BaseAnnouncementBean announcementReference;
    private String candidateEmail;
    private String status;

    protected ApplicationBean(BaseAnnouncementBean a, String email, String s, String id) {
        this.announcementReference = a;
        setCandidateEmail(email);
        this.status = s;
    }

    public BaseAnnouncementBean getAnnouncementReference() {
        return announcementReference;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public void setCandidateEmail(String candidateEmail) {

        if (!EmailChecker.isValidEmail(candidateEmail)) {
            throw new IllegalArgumentException("Invalid email: " + candidateEmail);
        }

        this.candidateEmail = candidateEmail;
    }

    public void setAnnouncementReference(BaseAnnouncementBean announcementReference) {
        this.announcementReference = announcementReference;
    }

}
