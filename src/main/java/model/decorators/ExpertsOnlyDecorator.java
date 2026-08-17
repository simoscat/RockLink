package model.decorators;

import model.Announcement;

public class ExpertsOnlyDecorator extends AnnouncementDecorator{
    public ExpertsOnlyDecorator(Announcement wrappedAnnouncement) {
        super(wrappedAnnouncement);
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
