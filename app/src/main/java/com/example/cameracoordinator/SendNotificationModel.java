package com.example.cameracoordinator;

public class SendNotificationModel {

    private String body,title,topic;

    public SendNotificationModel(String body, String title,String topic) {
        this.body = body;
        this.title = title;
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
