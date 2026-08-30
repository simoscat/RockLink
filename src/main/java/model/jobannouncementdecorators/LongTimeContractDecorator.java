package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class LongTimeContractDecorator extends JobAnnouncementDecorator {

    public LongTimeContractDecorator(JobAnnouncement wrappedJobAnnouncement) {
        super(wrappedJobAnnouncement);
    }

    @Override
    public String getTitle(){
        String old = super.getTitle();

        return applyLTCDecorator(old);
    }

    private String applyLTCDecorator(String old){
        return "[Long Time Contract] " + old;
    }
}
