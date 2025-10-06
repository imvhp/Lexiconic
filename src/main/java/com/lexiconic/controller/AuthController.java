package com.lexiconic.controller;

import com.lexiconic.domain.entity.Users;
import com.lexiconic.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/home")
    public String dashboard() {
        return "home";
    }

    @GetMapping("/auth/register")
    public String register(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute Users user) {
        userService.register(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute Users user,
                        HttpServletResponse response) {
        String token = userService.verify(user);

        // set JWT cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // true when HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // redirect after login
        return "redirect:/home";
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out");
    }
}
