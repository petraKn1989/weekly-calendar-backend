package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddItemRequest;
import com.example.demo.dto.CreateListRequest;
import com.example.demo.dto.DeleteItemsRequest;
import com.example.demo.dto.UpdateTextRequest;
import com.example.demo.entity.ShoppingItem;
import com.example.demo.entity.ShoppingSession;
import com.example.demo.service.ShoppingService;


@RestController
@RequestMapping("/api/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    // Endpoint pro vytvoření session
    @PostMapping("/session")
    public ResponseEntity<ShoppingSession> createSession() {
        ShoppingSession savedSession = shoppingService.createSession();
        return ResponseEntity.ok(savedSession);
    }

    // Endpoint pro vytvoření seznamu
    @PostMapping("/list/create")
    public ResponseEntity<?> createList(@RequestBody CreateListRequest request) {
        Map<String, Object> responseData = shoppingService.createList(request);
        return ResponseEntity.ok(responseData);
    }

    // Přidání položky
    @PostMapping("/item/add")
    public ResponseEntity<ShoppingItem> addItem(@RequestBody AddItemRequest request) {
        ShoppingItem savedItem = shoppingService.addItemToList(request);
        return ResponseEntity.ok(savedItem);
    }

    // OPRAVENO: Přidán @RequestParam UUID sessionId pro ověření bezpečnosti
    @PatchMapping("/item/{id}/toggle")
    public ResponseEntity<ShoppingItem> toggleItem(
            @PathVariable UUID id, 
            @RequestParam UUID sessionId) {
        ShoppingItem updatedItem = shoppingService.toggleItemChecked(id, sessionId);
        return ResponseEntity.ok(updatedItem);
    }

    // OPRAVENO: Přidán @RequestParam UUID sessionId pro ověření bezpečnosti
    @PutMapping("/item/{id}/update-text")
    public ResponseEntity<ShoppingItem> updateItemText(
            @PathVariable UUID id, 
            @RequestParam UUID sessionId,
            @RequestBody UpdateTextRequest request) {
        
        ShoppingItem updatedItem = shoppingService.updateItemText(id, sessionId, request.getText());
        return ResponseEntity.ok(updatedItem);
    }

    // OPRAVENO: Přidán @RequestParam UUID sessionId pro ověření bezpečnosti
    @DeleteMapping("/items/delete")
    public ResponseEntity<String> deleteItems(
            @RequestParam UUID sessionId,
            @RequestBody DeleteItemsRequest request) {
        shoppingService.deleteItems(request.getItemIds(), sessionId);
        return ResponseEntity.ok("Vybrané položky byly úspěšně smazány.");
    }

    // OPRAVENO: Přidán @RequestParam UUID sessionId pro ověření bezpečnosti
    @DeleteMapping("/item/{id}")
    public ResponseEntity<String> deleteSingleItem(
            @PathVariable UUID id,
            @RequestParam UUID sessionId) {
        shoppingService.deleteSingleItem(id, sessionId);
        return ResponseEntity.ok("Položka byla úspěšně smazána.");
    }

    // Smazání všech položek v konkrétním seznamu
    @DeleteMapping("/items/clear-list")
    public ResponseEntity<String> clearList(
            @RequestParam UUID sessionId,
            @RequestParam Integer listId) {
        
        shoppingService.deleteAllItemsInList(sessionId, listId);
        return ResponseEntity.ok("Celý nákupní seznam byl úspěšně vyčištěn.");
    }

    // Získání VŠECH položek v dané relaci
    @GetMapping("/session/{sessionId}/items")
    public ResponseEntity<List<ShoppingItem>> getSessionItems(@PathVariable UUID sessionId) {
        List<ShoppingItem> items = shoppingService.getItemsBySession(sessionId);
        return ResponseEntity.ok(items);
    }

    // Získání položek pouze ze specifického seznamu
    @GetMapping("/list")
    public ResponseEntity<List<ShoppingItem>> getListItems(
            @RequestParam UUID sessionId,
            @RequestParam Integer listId) {
        
        List<ShoppingItem> items = shoppingService.getItemsByList(sessionId, listId);
        return ResponseEntity.ok(items);
    }

    // Odfajfkování všeho v seznamu
    @PatchMapping("/items/check-all")
    public ResponseEntity<List<ShoppingItem>> checkAllItems(
            @RequestParam UUID sessionId,
            @RequestParam Integer listId) {
        
        List<ShoppingItem> updatedItems = shoppingService.checkAllItemsInList(sessionId, listId);
        return ResponseEntity.ok(updatedItems);
    }

    // Přejmenování celého seznamu
    @PutMapping("/list/rename")
    public ResponseEntity<List<ShoppingItem>> renameList(
            @RequestParam UUID sessionId,
            @RequestParam Integer listId,
            @RequestBody Map<String, String> body) {
        
        String newListName = body.get("listName");
        if (newListName == null || newListName.trim().isEmpty()) {
            throw new RuntimeException("Název seznamu nesmí být prázdný!");
        }

        List<ShoppingItem> updatedItems = shoppingService.updateListName(sessionId, listId, newListName);
        return ResponseEntity.ok(updatedItems);
    }
}