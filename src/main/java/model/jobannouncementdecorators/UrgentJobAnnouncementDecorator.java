package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class UrgentJobAnnouncementDecorator extends JobAnnouncementDecorator {

    public UrgentJobAnnouncementDecorator(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        String old = super.getTitle();

        return applyUrgentDecorator(old);
    }

    private String applyUrgentDecorator(String old){
        return "[Urgent] " + old;
    }
}
