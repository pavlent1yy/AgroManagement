package com.agro.service;

import com.agro.dto.UserForm;
import com.agro.entity.User;
import com.agro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Transactional
    public void create(UserForm form, String currentUsername) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("Пароль обязателен для нового пользователя");
        }

        User user = User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .fullName(form.getFullName())
                .role(form.getRole())
                .active(Boolean.TRUE.equals(form.getActive()))
                .build();
        userRepository.save(user);
        auditService.log("CREATE_USER", "Создан пользователь " + user.getUsername(), currentUsername);
    }

    @Transactional
    public void update(UserForm form, String currentUsername) {
        User user = userRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (userRepository.existsByUsernameAndIdNot(form.getUsername(), form.getId())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        user.setUsername(form.getUsername());
        user.setFullName(form.getFullName());
        user.setRole(form.getRole());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        user.setActive(Boolean.TRUE.equals(form.getActive()));
        userRepository.save(user);
        auditService.log("UPDATE_USER", "Обновлен пользователь " + user.getUsername(), currentUsername);
    }

    @Transactional
    public void toggleActive(Long userId, String currentUsername) {
        if (userId == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не задан");
        }
        User current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Текущий пользователь не найден"));
        if (current.getId().equals(userId)) {
            throw new IllegalArgumentException("Нельзя заблокировать собственную учетную запись");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setActive(!user.isActive());
        userRepository.save(user);
        auditService.log(user.isActive() ? "UNBLOCK_USER" : "BLOCK_USER",
                (user.isActive() ? "Разблокирован пользователь " : "Заблокирован пользователь ") + user.getUsername(),
                currentUsername);
    }
}
