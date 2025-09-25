package com.login_module.login_module.controller;

import com.login_module.login_module.model.Role;
import com.login_module.login_module.model.User;
import com.login_module.login_module.repository.RoleRepository;
import com.login_module.login_module.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador para gerenciar as rotas e funcionalidades do painel de administração.
 * A anotação @RequestMapping("/admin") garante que todas as rotas aqui
 * sejam prefixadas com /admin, sendo protegidas pelo SecurityConfig.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * READ: Exibe o painel principal do administrador com a lista de usuários.
     */
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("content", "fragments/admin-dashboard");
        return "layout";
    }

    /**
     * CREATE (Formulário): Exibe o formulário para adicionar um novo usuário.
     */
    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("pageTitle", "Adicionar Novo Usuário");
        model.addAttribute("content", "fragments/user-form");
        return "layout";
    }

    /**
     * UPDATE (Formulário): Exibe o formulário para editar um usuário existente.
     */
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("pageTitle", "Editar Usuário");
            model.addAttribute("content", "fragments/user-form");
            return "layout";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado.");
        return "redirect:/admin/dashboard";
    }

    /**
     * CREATE / UPDATE (Lógica): Salva um novo usuário ou atualiza um existente.
     */
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "rawPassword", required = false) String rawPassword,
                           @RequestParam(value = "roleIds", required = false) Set<String> roleIds,
                           RedirectAttributes redirectAttributes) {

        // A senha só é atualizada se um novo valor for fornecido.
        // Isso evita que a senha seja apagada ao editar um usuário sem preencher o campo.
        if (rawPassword != null && !rawPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        // Converte os IDs de roles recebidos do formulário em objetos Role.
        Set<Role> assignedRoles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            assignedRoles = roleIds.stream()
                    .map(roleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        }
        user.setRoles(assignedRoles);

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário salvo com sucesso!");
        return "redirect:/admin/dashboard";
    }

    /**
     * DELETE: Exclui um usuário com base no ID.
     */
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário excluído com sucesso!");
        return "redirect:/admin/dashboard";
    }
}