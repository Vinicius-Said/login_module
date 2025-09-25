package com.login_module.login_module.repository;

import com.login_module.login_module.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Interface de repositório para operações CRUD na entidade Role.
 * O Spring Data implementará os métodos automaticamente.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
    // Método customizado para buscar um perfil pelo nome.
    Role findByName(String name);
}