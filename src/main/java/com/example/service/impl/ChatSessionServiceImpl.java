package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.ChatbotConstants;
import com.example.entity.ChatSession;
import com.example.mapper.ChatSessionMapper;
import com.example.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public ChatSession getSession(String sessionId) {
        return super.getById(sessionId);
    }

    @Override
    public void saveSession(ChatSession session) {
        stringRedisTemplate.opsForValue().set(ChatbotConstants.CONVERSATION_ID_KEY + session.getId(), String.valueOf(session.getCreateBy()),
                ChatbotConstants.EXPIRE_TIME, TimeUnit.SECONDS);
        super.save(session);
    }
}
