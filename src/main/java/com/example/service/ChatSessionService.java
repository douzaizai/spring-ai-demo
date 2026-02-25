package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.ChatSession;

public interface ChatSessionService extends IService<ChatSession> {
    ChatSession getSession(String sessionId);

    void saveSession(ChatSession session);
}
