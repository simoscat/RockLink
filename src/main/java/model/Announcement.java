package model;

import java.util.Date;

public abstract class Announcement {

    private String id;
    private String title;
    private String content;
    private Date date;
    private AnnouncementStatus status;

    protected Announcement(String id, String title, String content, Date date, AnnouncementStatus status){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    //TODO

}
