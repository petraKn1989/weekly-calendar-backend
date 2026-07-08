package com.example.demo.dto;

import java.util.UUID;

public class CreateListRequest {

    private UUID sessionId;   // K jakému sdílenému odkazu to patří
    private String listName;  // Např. "Nákup Opava"

    // --- GETTERY A SETTERY ---
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public String getListName() { return listName; }
    public void setListName(String listName) { this.listName = listName; }

}
