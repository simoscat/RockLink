package model.jobAnnouncementDecorators;

import model.JobAnnouncement;

public class LongTimeContractDecoratorJob extends JobAnnouncementDecorator {

    public LongTimeContractDecoratorJob(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        String old = super.getTitle();

        return applyUrgentDecorator(old);
    }

    private String applyUrgentDecorator(String old){
        return "[Long Time Contract] " + old;
    }
}
