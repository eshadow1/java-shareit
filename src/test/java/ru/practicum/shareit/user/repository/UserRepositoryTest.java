package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforEach() {
        user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        var getUser = userRepository.findByEmail(user.getEmail());

        assertEquals(getUser.get().getId(), user.getId());
        assertEquals(getUser.get().getName(), user.getName());
        assertEquals(getUser.get().getEmail(), user.getEmail());
    }
}