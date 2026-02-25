package com.example.event;

import com.example.entity.ChatMessage;
import org.springframework.context.ApplicationEvent;

public class SaveChatMessageEvent extends ApplicationEvent {
    private final ChatMessage message;

    public SaveChatMessageEvent(Object source, ChatMessage message) {
        super(source);
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
