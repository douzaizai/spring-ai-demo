package com.example.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatCompletionRequest {
    @NotNull
    private String sessionId;
    private String message;
}
