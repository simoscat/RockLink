package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class ExpertsOnlyDecorator extends JobAnnouncementDecorator {

    public ExpertsOnlyDecorator(JobAnnouncement wrappedJobAnnouncement) {
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
