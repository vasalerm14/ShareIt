package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
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

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}