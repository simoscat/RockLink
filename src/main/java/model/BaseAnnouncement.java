package model;

import engineering.enums.AnnouncementStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class BaseAnnouncement implements Announcement {

    private String title;
    private String content;
    private LocalDateTime date;
    private AnnouncementStatus status;
    private Artist hiredArtist = null;
    private LocalDateTime publishDate;

    protected BaseAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status,
                               LocalDateTime publishDate, Artist hiredArtist) {
        this(title, content, date, status, publishDate);
        this.hiredArtist = hiredArtist;
    }

    protected BaseAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status,
                               LocalDateTime publishDate){
        this(title, content, date, status);
        this.publishDate = publishDate;
    }


    protected BaseAnnouncement(String title, String content, LocalDateTime date, AnnouncementStatus status) {
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

    public LocalDateTime getAnnouncementDate(){
        return this.date;
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

    public Artist whoWasHired(){
        return this.hiredArtist;
    }

    @Override
    public LocalDateTime getAnnouncementPublishDate(){
        return this.publishDate;
    }

    @Override
    public void publishNow(){
        this.publishDate = LocalDateTime.now(ZoneId.systemDefault());
    }

}
