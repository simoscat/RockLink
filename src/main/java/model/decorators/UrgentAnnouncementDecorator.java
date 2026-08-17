package model.decorators;

import model.Announcement;

public class UrgentAnnouncementDecorator extends AnnouncementDecorator {

    public UrgentAnnouncementDecorator(Announcement wrappedAnnouncement) {
        super(wrappedAnnouncement);
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
