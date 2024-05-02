package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(int userId);

    UserDto saveNewUser(UserDto userDto);

    UserDto updateUser(int userId, UserDto userDto);

    void deleteUser(int userId);
}