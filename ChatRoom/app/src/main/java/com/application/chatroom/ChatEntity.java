package com.application.chatroom;

public class ChatEntity {

    private String type;
    private String sendname;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendname() {
        return sendname;
    }

    public void setSendname(String sendname) {
        this.sendname = sendname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWhosend() {
        return whosend;
    }

    public void setWhosend(String whosend) {
        this.whosend = whosend;
    }

    private String whosend;
}
