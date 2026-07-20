package com.travel.controller;

import com.travel.dto.RegisterDto;
import com.travel.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDto dto,
                           BindingResult result, Model model) {
        if (result.hasErrors()) return "auth/register";

        if (authService.emailExists(dto.getEmail())) {
            model.addAttribute("emailError", "Email already registered");
            return "auth/register";
        }

        authService.register(dto);
        return "redirect:/login?registered=true";
    }
}
