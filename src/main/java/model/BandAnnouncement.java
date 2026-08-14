package model;

import java.util.Date;
import java.util.List;

public class BandAnnouncement extends Announcement{

    private Band band;
    private List<Instrument> instruments;


    protected BandAnnouncement(String id, String title, String content, Date date, AnnouncementStatus status,
                               Band band, List<Instrument> instruments) {
        super(id, title, content, date, status);
        this.band = band;
        this.instruments = instruments;
    }

    //TODO
}
