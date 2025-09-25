package com.login_module.login_module.controller;

import com.login_module.login_module.dto.UserDto;
import com.login_module.login_module.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gerenciar as rotas públicas de autenticação,
 * como login, registro e a página inicial.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Exibe a página inicial da aplicação.
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("content", "fragments/home-content");
        return "layout";
    }

    /**
     * Exibe a página de login.
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("content", "fragments/login-content");
        return "layout";
    }

    /**
     * Exibe o formulário de registro de novos usuários.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("content", "fragments/register-content");
        return "layout";
    }

    /**
     * Processa os dados do formulário de registro e salva o novo usuário.
     */
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
        // Validação simples para verificar se o username já existe.
        if (userService.findByUsername(userDto.getUsername()) != null) {
            model.addAttribute("errorMessage", "Este nome de usuário já está em uso.");
            model.addAttribute("user", userDto);
            model.addAttribute("content", "fragments/register-content");
            return "layout";
        }

        userService.saveUser(userDto);
        // Usa RedirectAttributes para que a mensagem de sucesso sobreviva ao redirecionamento.
        redirectAttributes.addFlashAttribute("successMessage", "Usuário registrado com sucesso! Por favor, faça o login.");
        return "redirect:/login";
    }
}