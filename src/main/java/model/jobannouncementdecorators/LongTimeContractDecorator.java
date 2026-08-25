package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class LongTimeContractDecorator extends JobAnnouncementDecorator {

    public LongTimeContractDecorator(JobAnnouncement wrappedJobAnnouncement) {
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
