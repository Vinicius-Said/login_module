package com.login_module.login_module.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.Set;

/**
 * Representa a entidade 'User' que será persistida na coleção 'users' do MongoDB.
 */
@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;

    // A anotação @Indexed garante a unicidade do campo no banco de dados.
    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    // Este campo armazenará a senha já criptografada (hash).
    private String password;

    private boolean enabled = true;

    // Relacionamento com a coleção 'roles'. @DBRef armazena a referência ao invés do objeto inteiro.
    @DBRef
    private Set<Role> roles;
}