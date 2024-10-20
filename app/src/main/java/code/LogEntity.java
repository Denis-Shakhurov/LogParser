package code;

import java.util.Date;

public class LogEntity {
    private String ip;
    private String user;
    private Date date;
    private Event event;
    private int taskId;
    private Status status;

    public LogEntity(String ip, String user, Date data, Event event, int taskId, Status status) {
        this.ip = ip;
        this.user = user;
        this.date = data;
        this.event = event;
        this.taskId = taskId;
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public Event getEvent() {
        return event;
    }

    public int getTaskId() {
        return taskId;
    }

    public Status getStatus() {
        return status;
    }
}
