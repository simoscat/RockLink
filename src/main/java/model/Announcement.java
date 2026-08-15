package model;

import engineering.enums.AnnouncementStatus;
import engineering.enums.AnnouncementType;

import java.util.Date;

public abstract class Announcement {

    private String id;
    private String title;
    private String content;
    private Date date;
    private AnnouncementStatus status;
    private AnnouncementType type;

    protected Announcement(String id, String title, String content, Date date, AnnouncementStatus status,
                           AnnouncementType type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
        this.type = type;
    }

    //TODO

}
