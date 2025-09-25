package com.login_module.login_module.service;

import com.login_module.login_module.dto.UserDto;
import com.login_module.login_module.model.Role;
import com.login_module.login_module.model.User;
import com.login_module.login_module.repository.RoleRepository;
import com.login_module.login_module.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementação da lógica de negócio para operações com usuários.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um novo usuário a partir de um DTO, atribuindo o perfil padrão 'ROLE_USER'.
     */
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        // Criptografa a senha antes de salvar no banco.
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Atribui o perfil padrão de usuário.
        Role userRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}