package model;

public class LongTimeContractDecorator extends AnnouncementDecorator{

    public LongTimeContractDecorator(Announcement wrappedAnnouncement) {
        super(wrappedAnnouncement);
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
