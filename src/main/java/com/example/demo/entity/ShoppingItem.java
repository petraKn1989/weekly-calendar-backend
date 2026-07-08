package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "shopping_items")

public class ShoppingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private ShoppingSession session;

    @Column(name = "list_id", nullable = false)
    private Integer listId; // Tvoje vnitřní ID (1, 2...)

    @Column(name = "list_name", nullable = false)
    private String listName; // Název (Např. Nákup v Opavě)

   

    
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean checked = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        // U generovaného UUID se setter často nepoužívá, 
        // ale pro jistotu ho tu máte.
        this.id = id;
    }

    public ShoppingSession getSession() {
        return session;
    }

    public void setSession(ShoppingSession session) {
        this.session = session;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Pozor: U typu boolean se getter standardně pojmenuje "is..." místo "get..."
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        // U datumu vytvoření (created_at) se hodnota většinou nemění ručně,
        // ale Hibernate/Spring ho občas potřebuje nastavit.
        this.createdAt = createdAt;

    }

    


}
