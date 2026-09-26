package com.agro.controller;

import com.agro.dto.UserForm;
import com.agro.entity.Role;
import com.agro.entity.User;
import com.agro.repository.UserRepository;
import com.agro.service.AuditService;
import com.agro.service.BackupService;
import com.agro.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuditService auditService;
    private final BackupService backupService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("recentAudits", auditService.getRecentLogs());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        UserForm form = new UserForm();
        form.setActive(true);
        model.addAttribute("userForm", form);
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setRole(user.getRole());
        form.setActive(user.isActive());

        model.addAttribute("userForm", form);
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@Valid @ModelAttribute("userForm") UserForm form,
                           BindingResult bindingResult,
                           Model model,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "admin/user-form";
        }

        try {
            if (form.getId() == null) {
                userService.create(form, authentication.getName());
                redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно создан");
            } else {
                userService.update(form, authentication.getName());
                redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлен");
            }
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("username", "duplicate", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "admin/user-form";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.toggleActive(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Состояние пользователя изменено");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/backup")
    public String createBackup(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            var backupPath = backupService.createBackup();
            redirectAttributes.addFlashAttribute("successMessage",
                    "Резервная копия создана: " + backupPath);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
