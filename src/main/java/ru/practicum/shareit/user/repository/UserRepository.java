package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User saveNewUser(User user);

    User getUserById(int userId);

    void deleteUser(int userId);
}
