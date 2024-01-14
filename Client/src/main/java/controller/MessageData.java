package controller;

import java.time.LocalDateTime;

public class MessageData {
    private String content;
    private LocalDateTime timestamp;

    public MessageData(String content, LocalDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }
    public MessageData(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
