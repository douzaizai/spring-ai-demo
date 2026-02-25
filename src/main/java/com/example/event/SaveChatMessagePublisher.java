package com.example.event;

import com.example.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SaveChatMessagePublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(ChatMessage message) {
        SaveChatMessageEvent event = new SaveChatMessageEvent(this, message);
        applicationEventPublisher.publishEvent(event);
    }
}
