package com.example.controller;

import com.example.context.UserContextHolder;
import com.example.entity.ChatMessage;
import com.example.entity.ChatSession;
import com.example.event.SaveChatMessagePublisher;
import com.example.request.ChatCompletionRequest;
import com.example.response.Response;
import com.example.service.ChatSessionService;
import com.example.tools.DateTimeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    DeepSeekChatModel chatModel;

    @Autowired
    MessageWindowChatMemory chatMemoryManager;

    @Autowired
    VectorStore vectorStore;

    @Autowired
    ChatSessionService chatSessionService;

    @Autowired
    SaveChatMessagePublisher saveChatMessagePublisher;

    @GetMapping("/session")
    public Response<String> createSession() {
        String uuid = UUID.randomUUID().toString();
        ChatSession session = new ChatSession(uuid, UserContextHolder.get().getUserId());
        chatSessionService.saveSession(session);
        return Response.success(uuid);
    }

    @PostMapping("/completion")
    public Response<String> completion(@RequestBody ChatCompletionRequest request) {
        String sessionId = request.getSessionId();

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole(MessageType.USER.getValue());
        userMessage.setContent(request.getMessage());
        userMessage.setSessionId(sessionId);
        userMessage.setCreateTime(LocalDateTime.now());

        saveChatMessagePublisher.publishEvent(userMessage);

        String completion = ChatClient.builder(chatModel)
                .build().prompt()
                .user(request.getMessage())
                .tools(new DateTimeTools())
                .advisors(MessageChatMemoryAdvisor.builder(chatMemoryManager).conversationId(sessionId).build())
                .call()
                .chatResponse().getResult().getOutput().getText();

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setRole(MessageType.ASSISTANT.getValue());
        assistantMessage.setContent(completion);
        assistantMessage.setSessionId(sessionId);
        assistantMessage.setCreateTime(LocalDateTime.now());
        saveChatMessagePublisher.publishEvent(assistantMessage);

        return Response.success(completion);
    }

    @PostMapping("/rag/completion")
    public Response<String> generate(@RequestBody ChatCompletionRequest request) {
        String completion = ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .user(request.getMessage())
                .advisors(MessageChatMemoryAdvisor.builder(chatMemoryManager).conversationId(request.getSessionId()).build())
                .call()
                .chatResponse().getResult().getOutput().getText();
        return Response.success(completion);
    }

    @GetMapping(value = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        var prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

    @GetMapping("/ai/searchDay")
    public Map tool(@RequestParam String message) {
        String response = ChatClient.create(chatModel)
                .prompt(message)
                .tools(new DateTimeTools())
                .call()
                .content();

        return Map.of("response", response);
    }

}