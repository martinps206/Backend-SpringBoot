package com.proyectoDH.service;

import com.proyectoDH.dto.CredentialsDto;
import com.proyectoDH.dto.SignUpDto;
import com.proyectoDH.dto.UserDto;
import com.proyectoDH.dto.UsersDto;
import com.proyectoDH.entities.ConfirmationToken;
import com.proyectoDH.entities.User;
import com.proyectoDH.exceptions.AppException;
import com.proyectoDH.mapper.UserMapper;
import com.proyectoDH.repository.ConfirmationTokenRepository;
import com.proyectoDH.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public String findLoginById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getLogin();
        } else {

            return "Usuario no encontrado";
        }
    }

    public List<UsersDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UsersDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .login(user.getLogin())
                        .admin(user.getAdmin())
                        .build())
                .collect(Collectors.toList());
    }

    public User updateAdminStatus(Long userId, Boolean newAdminStatus) throws ChangeSetPersister.NotFoundException {
        try {
            User user = userRepository.getOne(userId);
            user.setAdmin(newAdminStatus);
            return userRepository.save(user);
        } catch (EntityNotFoundException e) {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(Long userId, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(userId, token);
        confirmationTokenRepository.save(confirmationToken);
    }


}
