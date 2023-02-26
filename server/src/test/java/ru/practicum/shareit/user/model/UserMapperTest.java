package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void fromUserDto() {
        var userDto = UserDto.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();
        var userId = 1;

        var user = UserMapper.fromUserDto(userDto, userId);

        var correctUser = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        assertEquals(user.getId(), correctUser.getId());
        assertEquals(user.getName(), correctUser.getName());
        assertEquals(user.getEmail(), correctUser.getEmail());
    }

    @Test
    void toUserDto() {
        var user = User.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        var userDto = UserMapper.toUserDto(user);

        var correctUserDto = UserDto.builder()
                .id(1)
                .name("Test")
                .email("test@test.test")
                .build();

        assertEquals(userDto.getName(), correctUserDto.getName());
        assertEquals(userDto.getEmail(), correctUserDto.getEmail());
    }
}