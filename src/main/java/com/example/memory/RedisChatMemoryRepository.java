package com.example.memory;

import com.example.constants.ChatbotConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class RedisChatMemoryRepository implements ChatMemoryRepository {
//    @Autowired
//    @Qualifier("chatRedisTemplate")
//    private RedisTemplate<String, List<Message>> chatRedisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> findConversationIds() {
        List<String> keys = new ArrayList<>();
        try (Cursor<String> cursor = stringRedisTemplate.scan(
                ScanOptions.scanOptions()
                        .match(ChatbotConstants.CONVERSATION_ID_KEY + "*") // 匹配模式
                        .count(1000) // 每次扫描数量
                        .build())) {

            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }
        return keys;
    }

    @SneakyThrows
    @Override
    public List<Message> findByConversationId(String conversationId) {
        String raw = stringRedisTemplate.opsForValue().get(ChatbotConstants.MESSAGE_KEY + conversationId);
        if (raw != null) {
            List<Map<String, Object>> list = objectMapper.readValue(raw, new TypeReference<List<Map<String, Object>>>() {
            });
            List<Message> messages = new ArrayList<>();
            list.forEach(map -> {
                Message message = null;
                String messageType = String.valueOf(map.get("messageType"));
                if (messageType.equals(MessageType.USER.name())) {
                    message = UserMessage.builder().text((String) map.get("text")).media((List<Media>) map.get("media"))
                            .metadata((Map<String, Object>) map.get("metadata")).build();
                } else if (messageType.equals(MessageType.SYSTEM.name())) {
                    message = SystemMessage.builder().text((String) map.get("text"))
                            .metadata((Map<String, Object>) map.get("metadata")).build();
                } else if (messageType.equals(MessageType.ASSISTANT.name())) {
                    message = AssistantMessage.builder().content((String) map.get("text")).media((List<Media>) map.get("media")).toolCalls((List<AssistantMessage.ToolCall>) map.get("toolCalls"))
                            .properties((Map<String, Object>) map.get("metadata")).build();
                } else if (messageType.equals(MessageType.TOOL.name())) {
                    message = ToolResponseMessage.builder().responses((List<ToolResponseMessage.ToolResponse>) map.get("responses"))
                            .metadata((Map<String, Object>) map.get("metadata")).build();
                }
                messages.add(message);
            });
            return messages;
        } else {
            return Collections.emptyList();
        }
//        return Optional.ofNullable(chatRedisTemplate.opsForValue().get(ChatbotConstants.MESSAGE_KEY + conversationId)).orElse(Collections.emptyList());
    }

    @SneakyThrows
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        String raw = objectMapper.writeValueAsString(messages);
        stringRedisTemplate.opsForValue().set(ChatbotConstants.MESSAGE_KEY + conversationId, raw, ChatbotConstants.EXPIRE_TIME, TimeUnit.SECONDS);
//        chatRedisTemplate.opsForValue().set(ChatbotConstants.MESSAGE_KEY + conversationId, messages, ChatbotConstants.EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        stringRedisTemplate.delete(ChatbotConstants.MESSAGE_KEY + conversationId);
    }
}
