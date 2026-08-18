package bean;

public abstract class ApplicationBean {
    
    private String announcementId;
    private String candidateEmail;
    private String status;

    public ApplicationBean() {
    }

    public ApplicationBean(String announcementId, String candidateEmail, String status) {
        this.announcementId = announcementId;
        this.candidateEmail = candidateEmail;
        this.status = status;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
