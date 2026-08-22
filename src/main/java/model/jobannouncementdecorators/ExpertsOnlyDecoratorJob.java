package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class ExpertsOnlyDecoratorJob extends JobAnnouncementDecorator {

    public ExpertsOnlyDecoratorJob(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        String old = super.getTitle();

        return applyExpertDecorator(old);
    }

    private String applyExpertDecorator(String old){
        return "[Experts only] " + old;
    }
}
