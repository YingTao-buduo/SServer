package com.yt.s_server.home;

public class Leave {
    private String reason;
    private String id;
    private String dateCreated;
    private String type;//[1 事假][2 病假][3 公假]
    private String status;//[CREATED][SUBMITTED][APPROVED][REJECTED][FINISHED]

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateCreated() { return dateCreated; }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
