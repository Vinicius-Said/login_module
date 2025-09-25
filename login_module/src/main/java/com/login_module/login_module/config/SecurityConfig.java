package com.login_module.login_module.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configura as regras centrais de segurança da aplicação, como proteção de rotas,
 * formulário de login e política de criptografia de senhas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define o BCrypt como o algoritmo para criptografar senhas.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura a cadeia de filtros de segurança do Spring Security.
     * É aqui que as permissões de acesso às URLs são definidas.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para simplificar (não ideal para produção)
                .authorizeHttpRequests(auth -> auth
                        // Libera o acesso público a rotas essenciais e recursos estáticos.
                        .requestMatchers("/", "/home", "/register", "/register/save").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // Protege as rotas de admin, exigindo o perfil 'ADMIN'.
                        // A ordem é importante: regras específicas devem vir antes das genéricas.
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Exige que qualquer outra requisição seja feita por um usuário autenticado.
                        .anyRequest().authenticated()
                )
                // Configura o formulário de login.
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                // Configura a funcionalidade de logout.
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}