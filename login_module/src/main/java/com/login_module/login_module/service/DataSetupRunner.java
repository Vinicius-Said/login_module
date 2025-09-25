package com.login_module.login_module.service;

import com.login_module.login_module.model.Role;
import com.login_module.login_module.model.User;
import com.login_module.login_module.repository.RoleRepository;
import com.login_module.login_module.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe utilitária que roda uma vez na inicialização da aplicação.
 * Seu propósito é garantir que os perfis (Roles) e um usuário administrador
 * padrão existam no banco de dados, facilitando a configuração inicial.
 */
@Component
public class DataSetupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSetupRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Garante que os perfis (Roles) essenciais existam no banco.
        setupRole("ROLE_ADMIN");
        setupRole("ROLE_USER");

        // Garante que o usuário administrador padrão exista.
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@seuprojeto.com");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Senha padrão para o admin

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_ADMIN"));
            adminUser.setRoles(roles);
            adminUser.setEnabled(true);

            userRepository.save(adminUser);
            System.out.println(">>> Usuário 'admin' padrão criado com sucesso!");
        }
    }

    private void setupRole(String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            roleRepository.save(new Role(null, roleName));
        }
    }
}