package model;

import engineering.enums.Event;

import java.time.LocalDateTime;

public class Notification {

    private User sender;
    private User receiver;
    private Event event;
    private LocalDateTime timeStamp;
    private JobAnnouncement jobAnnouncement;

    public Notification(User sender, User receiver, Event event, LocalDateTime timeStamp,
                        JobAnnouncement jobAnnouncement) {
        this.sender = sender;
        this.receiver = receiver;
        this.event = event;
        this.timeStamp = timeStamp;
        this.jobAnnouncement = jobAnnouncement;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public JobAnnouncement getJobAnnouncement() {
        return jobAnnouncement;
    }

}
