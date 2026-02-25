package com.example.event;

import com.example.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveChatMessageEventLister implements ApplicationListener<SaveChatMessageEvent> {

    private final ChatMessageService chatMessageService;

    @Async
    @Override
    public void onApplicationEvent(SaveChatMessageEvent event) {
        chatMessageService.save(event.getMessage());
    }
}