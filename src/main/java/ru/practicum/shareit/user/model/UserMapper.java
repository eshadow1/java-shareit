package ru.practicum.shareit.user.model;


import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static User fromUserDto(final UserDto userDto, final int userId) {
        return User.builder()
                .id(userId)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User update(final User user, final User newUser) {
        var tempUser = user.toBuilder();

        if (newUser.getName() != null && !newUser.getName().isEmpty()) {
            tempUser.name(newUser.getName());
        }

        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            tempUser.email(newUser.getEmail());
        }

        return tempUser.build();
    }

    private UserMapper() {
    }
}
