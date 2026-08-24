package bean;


import engineering.enums.Event;

import java.time.LocalDateTime;

public class NotificationBean {

    private String sender;
    private String receiver;
    private Event event;
    private LocalDateTime time;
    private JobAnnouncementBean jobAnnouncement;

    public NotificationBean(String sender, String receiver, Event event, LocalDateTime time,
                            JobAnnouncementBean jobAnnouncement) {
        this.sender = sender;
        this.receiver = receiver;
        this.event = event;
        this.time = time;
        this.jobAnnouncement = jobAnnouncement;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public JobAnnouncementBean getJobAnnouncement() {
        return jobAnnouncement;
    }

    public void setJobAnnouncement(JobAnnouncementBean jobAnnouncement) {
        this.jobAnnouncement = jobAnnouncement;
    }

}
