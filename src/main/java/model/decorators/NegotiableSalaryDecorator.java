package model.decorators;

import model.Announcement;

public class NegotiableSalaryDecorator extends AnnouncementDecorator{
    public NegotiableSalaryDecorator(Announcement wrappedAnnouncement) {
        super(wrappedAnnouncement);
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
