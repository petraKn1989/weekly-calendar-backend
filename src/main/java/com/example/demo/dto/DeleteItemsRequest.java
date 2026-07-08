package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class DeleteItemsRequest {

    private List<UUID> itemIds;

    public List<UUID> getItemIds() { return itemIds; }
    public void setItemIds(List<UUID> itemIds) { this.itemIds = itemIds; }

}
