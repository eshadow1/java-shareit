package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.exception.ContainsFalseException;

import java.util.List;
import java.util.Optional;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    public ItemRequest addItemRequest(ItemRequest itemRequest) {
        checkedUser(itemRequest.getRequestorId().getId());

        return itemRequestRepository.save(itemRequest);
    }

    public ItemRequest getItemRequest(int userId, int itemRequestId) {
        checkedItemRequestContains(itemRequestId);
        checkedUser(userId);

        return itemRequestRepository.findById(itemRequestId).get();
    }

    public List<ItemRequest> getAllItemRequestByUser(int userId) {
        var user = userRepository.findById(userId);
        checkedUser(userId, user);

        return itemRequestRepository.findItemRequestByRequestorId(user.get());
    }

    public List<ItemRequest> getItemRequestFromSize(int userId, int from, int size) {
        var user = userRepository.findById(userId);
        checkedUser(userId, user);

        return itemRequestRepository.findItemRequestByRequestorIdNot(user.get(),
                PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "created"))).getContent();
    }

    private void checkedItemRequestContains(int id) {
        if (itemRequestRepository.findById(id).isEmpty()) {
            throw new ContainsFalseException("Заказ с id " + id + " не найден");
        }
    }

    private void checkedUser(int userId, Optional<User> user) {
        if (user.isEmpty()) {
            throw new ContainsFalseException("Пользователь с id " + userId + " не найден");
        }
    }

    private void checkedUser(int userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ContainsFalseException("Пользователь с id " + userId + " не найден");
        }
    }
}
