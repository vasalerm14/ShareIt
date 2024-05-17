package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static int generatorId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public User saveNewUser(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("Пользователь для удаления не найден");
        }
        users.remove(userId);
    }
}
