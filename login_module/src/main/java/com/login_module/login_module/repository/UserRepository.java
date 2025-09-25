package com.login_module.login_module.repository;

import com.login_module.login_module.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Interface de repositório para operações CRUD na entidade User.
 */
public interface UserRepository extends MongoRepository<User, String> {
    // Métodos customizados para buscar um usuário por username ou email.
    // O uso de Optional é uma boa prática para evitar NullPointerExceptions.
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}