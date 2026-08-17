package bean;

public abstract class BaseAnnouncementBean {

    private String id;
    private String title;
    private String content;
    private String date;
    private String announcementStatus;
    private String hiredArtist;

    public BaseAnnouncementBean(String id, String title, String content, String date, String announcementStatus, String hiredArtist) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.announcementStatus = announcementStatus;
        this.hiredArtist = hiredArtist;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getAnnouncementStatus() {
        return announcementStatus;
    }

    public String getHiredArtist() {
        return hiredArtist;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAnnouncementStatus(String announcementStatus) {
        this.announcementStatus = announcementStatus;
    }

    public void setHiredArtist(String hiredArtist) {
        this.hiredArtist = hiredArtist;
    }

}
