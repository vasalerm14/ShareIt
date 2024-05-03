package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final List<String> emails = new ArrayList<>();

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : userRepository.getAllUsers()) {
            usersDto.add(UserMapper.toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto getUserById(int userId) {
        return UserMapper.toUserDto(userRepository.getUserById(userId));
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {

        validateUniqueEmail(userDto);
        User user = userRepository.saveNewUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User user = userRepository.getUserById(userId);
        String name = userDto.getName();
        String email = userDto.getEmail();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (email != null && !email.isBlank()) {
            if (!user.getEmail().equals(userDto.getEmail())) {
                validateUniqueEmail(userDto);
            }
            emails.remove(user.getEmail());
            user.setEmail(email);

        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(int id) {
        emails.remove(userRepository.getUserById(id).getEmail());
        userRepository.deleteUser(id);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (emails.contains(userDto.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с таким Email уже существует");
        }
        emails.add(userDto.getEmail());
    }
}