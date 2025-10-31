package com.yestravel.contactsmanager.dto;

import com.yestravel.contactsmanager.model.InteractionType;

public class InteractionRequest {
    private Long contactId;
    private String username;
    private InteractionType type;
    private String description;

    // Getters and setters
    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public InteractionType getType() { return type; }
    public void setType(InteractionType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
