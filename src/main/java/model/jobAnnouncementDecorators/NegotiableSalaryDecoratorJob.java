package model.jobAnnouncementDecorators;

import model.JobAnnouncement;

public class NegotiableSalaryDecoratorJob extends JobAnnouncementDecorator {
    public NegotiableSalaryDecoratorJob(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        String old = super.getTitle();

        return applyNegotiableDecorator(old);
    }

    private String applyNegotiableDecorator(String old){
        return "[Negotiable Salary] " + old;
    }
}
