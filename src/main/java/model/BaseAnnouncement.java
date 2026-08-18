package model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class BaseAnnouncement {

    private String title;
    private String content;
    private LocalDateTime date;
    private LocalDateTime publishDate;

    protected BaseAnnouncement(String title, String content, LocalDateTime date,
                               LocalDateTime publishDate) {
        this(title, content, date);
        this.publishDate = publishDate;
    }

    protected BaseAnnouncement(String title, String content, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.date = date;
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

    public LocalDateTime getAnnouncementPublishDate(){
        return this.publishDate;
    }

    public void publishNow(){
        this.publishDate = LocalDateTime.now(ZoneId.systemDefault());
    }

}
