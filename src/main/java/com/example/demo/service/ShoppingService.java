package com.example.demo.service;

import com.example.demo.dto.AddItemRequest;
import com.example.demo.dto.CreateListRequest;
import com.example.demo.entity.ShoppingItem;
import com.example.demo.entity.ShoppingSession;
import com.example.demo.repository.ShoppingItemRepository;
import com.example.demo.repository.ShoppingSessionRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ShoppingService {
    
    private final ShoppingSessionRepository sessionRepository;
    private final ShoppingItemRepository itemRepository;

    public ShoppingService(ShoppingSessionRepository sessionRepository, 
                           ShoppingItemRepository itemRepository) {
        this.sessionRepository = sessionRepository;
        this.itemRepository = itemRepository;
    }

    // Vytvoření nové session
    @Transactional
    public ShoppingSession createSession() {
        ShoppingSession newSession = new ShoppingSession();
        return sessionRepository.save(newSession);
    }

    // Vytvoření seznamu uvnitř session
    @Transactional
    public Map<String, Object> createList(CreateListRequest request) {
        // Validace délky názvu seznamu
        if (request.getListName() == null || request.getListName().trim().length() > 100) {
            throw new IllegalArgumentException("Název seznamu nesmí být prázdný a může mít maximálně 100 znaků!");
        }

        // Ověření existence session
        ShoppingSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session nenalezena!"));

        // Vygenerování unikátního čísla listu
        Integer generatedListId = (int) (Math.random() * 100000);

        // Vytvoření iniciačního záznamu
        ShoppingItem initItem = new ShoppingItem();
        initItem.setSession(session);
        initItem.setListId(generatedListId);
        initItem.setListName(request.getListName().trim());
        initItem.setText("_INIT_EMPTY_"); 
        initItem.setChecked(false);

        itemRepository.save(initItem);

        return Map.of(
            "listId", generatedListId,
            "listName", request.getListName().trim(),
            "status", "Seznam úspěšně vytvořen"
        );
    }

    // Přidání položky do seznamu
    @Transactional
    public ShoppingItem addItemToList(AddItemRequest request) {
        // Validace délky textu položky
        if (request.getText() == null || request.getText().trim().length() > 100) {
            throw new IllegalArgumentException("Název položky nesmí být prázdný a může mít maximálně 100 znaků!");
        }

        // Ověření, že session existuje
        ShoppingSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session nenalezena!"));

        // Bezpečné dohledání názvu seznamu v rámci dané session (optimalizováno pomocí repozitáře)
        String actualListName = itemRepository.findBySession_IdAndListId(request.getSessionId(), request.getListId()).stream()
                .map(ShoppingItem::getListName)
                .findFirst()
                .orElse("Neznámý seznam");

        // Vytvoření nové položky
        ShoppingItem newItem = new ShoppingItem();
        newItem.setSession(session);
        newItem.setListId(request.getListId());
        newItem.setListName(actualListName);
        newItem.setText(request.getText().trim());
        newItem.setChecked(false);

        return itemRepository.save(newItem);
    }

    // Změna stavu zaškrtnutí (Bezpečné: Ověřuje vlastnictví položky přes sessionId)
    @Transactional
    public ShoppingItem toggleItemChecked(UUID itemId, UUID sessionId) {
        ShoppingItem item = itemRepository.findByIdAndSession_Id(itemId, sessionId)
                .orElseThrow(() -> new SecurityException("Neautorizovaný přístup nebo položka neexistuje!"));

        item.setChecked(!item.isChecked());
        return itemRepository.save(item);
    }

    // Úprava textu položky (Bezpečné: Ověřuje vlastnictví položky přes sessionId)
    @Transactional
    public ShoppingItem updateItemText(UUID itemId, UUID sessionId, String newText) {
        if (newText == null || newText.trim().length() > 100) {
            throw new IllegalArgumentException("Text položky nesmí být prázdný a může mít maximálně 100 znaků!");
        }

        ShoppingItem item = itemRepository.findByIdAndSession_Id(itemId, sessionId)
                .orElseThrow(() -> new SecurityException("Neautorizovaný přístup nebo položka neexistuje!"));

        item.setText(newText.trim());
        return itemRepository.save(item);
    }

    // Smazání jedné položky (Bezpečné: Ověřuje vlastnictví)
    @Transactional
    public void deleteSingleItem(UUID itemId, UUID sessionId) {
        ShoppingItem item = itemRepository.findByIdAndSession_Id(itemId, sessionId)
                .orElseThrow(() -> new SecurityException("Neautorizovaný přístup nebo položka neexistuje!"));
        
        itemRepository.delete(item);
    }

    // Hromadné smazání vybraných položek (Bezpečné: Ověřuje vlastnictví u každé z nich)
    @Transactional
    public void deleteItems(List<UUID> itemIds, UUID sessionId) {
        for (UUID itemId : itemIds) {
            ShoppingItem item = itemRepository.findByIdAndSession_Id(itemId, sessionId)
                    .orElseThrow(() -> new SecurityException("Neautorizovaný přístup k položce: " + itemId));
            itemRepository.delete(item);
        }
    }

    // Smazání celého seznamu
    @Transactional
    public void deleteAllItemsInList(UUID sessionId, Integer listId) {
        ShoppingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session nenalezena!"));

        itemRepository.deleteByListIdAndSession(listId, session);
    }

    // Získání položek pro celou session
    public List<ShoppingItem> getItemsBySession(UUID sessionId) {
        return itemRepository.findBySession_Id(sessionId);
    }

    // Získání položek pro konkrétní seznam
    public List<ShoppingItem> getItemsByList(UUID sessionId, Integer listId) {
        return itemRepository.findBySession_IdAndListId(sessionId, listId);
    }

    // Zaškrtnutí všech položek v seznamu najednou
    @Transactional
    public List<ShoppingItem> checkAllItemsInList(UUID sessionId, Integer listId) {
        ShoppingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session nenalezena!"));

        List<ShoppingItem> items = itemRepository.findBySession_IdAndListId(sessionId, listId);

        if (items.isEmpty()) {
            throw new RuntimeException("V tomto seznamu nebyly nalezeny žádné položky!");
        }

        for (ShoppingItem item : items) {
            item.setChecked(true);
        }

        return itemRepository.saveAll(items);
    }

    // Přejmenování celého seznamu
    @Transactional
    public List<ShoppingItem> updateListName(UUID sessionId, Integer listId, String newListName) {
        if (newListName == null || newListName.trim().length() > 100) {
            throw new IllegalArgumentException("Název seznamu nesmí být prázdný a může mít maximálně 100 znaků!");
        }

        ShoppingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session nenalezena!"));

        List<ShoppingItem> items = itemRepository.findBySession_IdAndListId(sessionId, listId);

        if (items.isEmpty()) {
            throw new RuntimeException("V tomto seznamu nebyly nalezeny žádné položky pro změnu názvu!");
        }

        for (ShoppingItem item : items) {
            item.setListName(newListName.trim());
        }

        return itemRepository.saveAll(items);
    }
}