package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class UrgentDecorator extends JobAnnouncementDecorator {

    public UrgentDecorator(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        return "[Urgent] "+super.getTitle();
    }

    @Override
    public String getContent(){
        return super.getContent() + "\nPlease respond as soon as possible.";
    }

}
