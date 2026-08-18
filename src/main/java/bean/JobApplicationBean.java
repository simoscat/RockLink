package bean;

import java.math.BigDecimal;

public class JobApplicationBean extends ApplicationBean{

    private BigDecimal raiseOffer;
    private JobAnnouncementBean jobAnnouncementReference;

    public JobApplicationBean(BigDecimal raiseOffer, JobAnnouncementBean jobAnnouncementReference) {
        this.raiseOffer = raiseOffer;
        this.jobAnnouncementReference = jobAnnouncementReference;
    }

    public BigDecimal getRaiseOffer() {
        return raiseOffer;
    }

    public void setRaiseOffer(BigDecimal raiseOffer) {
        this.raiseOffer = raiseOffer;
    }

    public JobAnnouncementBean getJobAnnouncementReference() {
        return jobAnnouncementReference;
    }

    public void setJobAnnouncementReference(JobAnnouncementBean jobAnnouncementReference) {
        this.jobAnnouncementReference = jobAnnouncementReference;
    }


}
