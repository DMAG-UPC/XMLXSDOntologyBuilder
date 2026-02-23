package org.example.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DownloaderSettings {
    private int messagesPerMinute;
    
    public DownloaderSettings() {
        this.messagesPerMinute = 10;
    }

    public void decreaseMessagesPerMinute() {
        this.messagesPerMinute = Math.max(1, this.messagesPerMinute - 1);
    }
}
