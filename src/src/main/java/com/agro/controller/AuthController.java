package com.agro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
        boolean isAgronomist = hasRole(authentication, "ROLE_AGRONOMIST");
        boolean isMechanic = hasRole(authentication, "ROLE_MECHANIC");
        boolean isStorekeeper = hasRole(authentication, "ROLE_STOREKEEPER");

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        if (isAgronomist) {
            return "redirect:/agronomist/dashboard";
        }
        if (isMechanic) {
            return "redirect:/mechanic/dashboard";
        }
        if (isStorekeeper) {
            return "redirect:/storekeeper/dashboard";
        }
        return "redirect:/login";
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}
