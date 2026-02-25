package com.example.utils;

import com.example.entity.ChatMessage;
import org.springframework.ai.chat.messages.*;

public class MessageWrapper {
    private MessageWrapper() {
    }

    public static Message wrap(ChatMessage message) {
        return wrap(message.getContent(), message.getRole());
    }

    public static Message wrap(String content, String role) {
        if (MessageType.USER.getValue().equals(role)) {
            return new UserMessage(content);
        } else if (MessageType.ASSISTANT.getValue().equals(role)) {
            return new AssistantMessage(content);
        } else if (MessageType.SYSTEM.getValue().equals(role)) {
            return new SystemMessage(content);
        }
        return null;
    }
}
