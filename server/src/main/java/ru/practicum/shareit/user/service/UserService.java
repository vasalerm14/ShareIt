package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(int userId) {
        log.info("Получение пользователя по идентификатору {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
        return UserMapper.toUserDto(user);
    }

    public UserDto saveNewUser(UserDto userDto) {
        log.info("Создание нового пользователя {}", userDto.getName());
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(int userId, UserDto userDto) {
        log.info("Обновление существующего пользователя {}", userDto.getName());
        User oldUser = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
        String name = userDto.getName();
        String email = userDto.getEmail();
        if (name != null && !name.isBlank()) {
            oldUser.setName(name);
        }
        if (email != null && !email.isBlank()) {
            oldUser.setEmail(email);
        }
        return UserMapper.toUserDto(oldUser);
    }

    public void deleteUser(int id) {
        log.info("Удаление пользователя по идентификатору {}", id);
        userRepository.deleteById(id);
    }
}