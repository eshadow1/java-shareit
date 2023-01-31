package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Transactional
    @Query("SELECT i " +
            "FROM Item AS i "+
            "WHERE i.isAvailable = true AND (lower(i.name) LIKE lower(?1)  OR lower(i.description) LIKE lower(?1)) " +
            "ORDER BY i.id")
    List<Item> searchItemsBy(String tempText);

    @Transactional
    @Query("SELECT i " +
            "FROM Item AS i "+
            "WHERE i.owner = ?1 " +
            "ORDER BY i.id")
    List<Item> findAllByOwnerId(int userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Item i " +
            "SET i.name = ?2, i.description = ?3, i.isAvailable = ?4 " +
            "WHERE i.id = ?1")
    void update(int id, String name, String description, Boolean isAvailable);
}
