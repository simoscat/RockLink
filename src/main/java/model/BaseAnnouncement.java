package model;

import engineering.enums.AnnouncementStatus;

import java.util.Date;

public abstract class BaseAnnouncement implements Announcement {

    private String id;
    private String title;
    private String content;
    private Date date;
    private AnnouncementStatus status;
    private Artist hiredArtist = null;

    protected BaseAnnouncement(String id, String title, String content, Date date, AnnouncementStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    public String getTitle(){
        return this.title;
    }
    public String getContent(){
        return this.content;
    }
    public Date getDate(){
        return this.getDate();
    }

    public void openAnnouncement(){
        this.status = AnnouncementStatus.OPEN;
    }


    public void closeAnnouncement(){
        this.status = AnnouncementStatus.CLOSED;
    }

    public AnnouncementStatus getStatus(){
        return this.status;
    }

    public void hireArtist(Artist artist){
        this.hiredArtist = artist;
        this.status = AnnouncementStatus.FILLED;
    }

}
