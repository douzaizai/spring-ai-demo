package com.example.config;

import com.example.memory.RedisChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Value("${chat.memory.size:10}")
    private int memorySize;

    @Bean
    public MessageWindowChatMemory chatMemory(RedisChatMemoryRepository redisChatMemoryRepository) {
        return MessageWindowChatMemory.builder().chatMemoryRepository(redisChatMemoryRepository).maxMessages(memorySize).build();
    }

}