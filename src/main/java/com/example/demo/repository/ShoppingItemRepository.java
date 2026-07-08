package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ShoppingItem;
import com.example.demo.entity.ShoppingSession;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID> {

    void deleteByListIdAndSession(Integer listId, ShoppingSession session);

    // OPRAVA: V entitě máte pole "private ShoppingSession session;", proto Hibernate hledá "Session_Id" nebo "Session"
    List<ShoppingItem> findBySession_Id(UUID sessionId);

    // OPRAVA: To samé platí i zde pro kombinovaný dotaz
    List<ShoppingItem> findBySession_IdAndListId(UUID sessionId, Integer listId);

    // OPRAVA: A pro naši nejdůležitější bezpečností kontrolu
    Optional<ShoppingItem> findByIdAndSession_Id(UUID itemId, UUID sessionId);

}
