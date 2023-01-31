package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.email = ?2 WHERE u.id = ?1")
    void updateEmail(Integer id, String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.name = ?2 WHERE u.id = ?1")
    void updateName(Integer id, String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.name = ?2, u.email = ?3 WHERE u.id = ?1")
    void updateNameAndEmail(Integer id, String name, String email);
}
