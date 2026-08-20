package bean;

import java.time.LocalDateTime;

public abstract class BaseAnnouncementBean {

    private String title;
    private String content;
    private LocalDateTime date;
    private LocalDateTime publishDate;

    public BaseAnnouncementBean(String title, String content, LocalDateTime date, LocalDateTime publishDate) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.publishDate = publishDate;
    }

    public BaseAnnouncementBean(String title, String content, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

}
