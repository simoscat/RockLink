package model.jobannouncementdecorators;

import model.JobAnnouncement;

public class NegotiableSalaryDecorator extends JobAnnouncementDecorator {
    public NegotiableSalaryDecorator(JobAnnouncement wrappedJobAnnouncement) {
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
