package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addItemRequest(ItemRequest itemRequest);

    ItemRequest getItemRequest(int userId, int itemRequestId);

    List<ItemRequest> getAllItemRequestByUser(int userId);

    List<ItemRequest> getItemRequestFromSize(int userId, int from, int size);
}