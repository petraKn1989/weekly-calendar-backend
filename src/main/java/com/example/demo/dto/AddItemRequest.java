package com.example.demo.dto;

import java.util.UUID;

public class AddItemRequest {
    private UUID sessionId;
    private Integer listId;
    private String text;

    // Gettery a Settery
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public Integer getListId() { return listId; }
    public void setListId(Integer listId) { this.listId = listId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

}
