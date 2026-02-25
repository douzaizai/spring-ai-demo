package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService extends IService<ChatMessage> {
    List<ChatMessage> getMessagesBySession(String sessionId, int limit);

    void saveMessage(ChatMessage message);
}
