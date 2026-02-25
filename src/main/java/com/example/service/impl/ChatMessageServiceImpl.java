package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.ChatMessage;
import com.example.mapper.ChatMessageMapper;
import com.example.service.ChatMessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    @Override
    public List<ChatMessage> getMessagesBySession(String sessionId, int limit) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId).orderByDesc("create_time").last("LIMIT " + limit);
        List<ChatMessage> messages = super.getBaseMapper().selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(messages)) {
            Collections.reverse(messages);
        }
        return messages;
    }

    @Override
    public void saveMessage(ChatMessage message) {
        super.save(message);
    }
}
