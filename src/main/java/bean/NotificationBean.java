package bean;


import engineering.enums.Event;

import java.time.LocalDateTime;

public class NotificationBean {
    
    private String sender;
    private String receiver;
    private Event event;
    private LocalDateTime time;
    
    public NotificationBean(String sender, String receiver, Event event, LocalDateTime time) {
        this.sender = sender;
        this.receiver = receiver;
        this.event = event;
        this.time = time;
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

}
