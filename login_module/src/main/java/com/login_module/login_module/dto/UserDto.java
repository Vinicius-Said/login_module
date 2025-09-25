package com.login_module.login_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para transportar dados do formulário de registro.
 * Serve como uma camada de segurança e desacoplamento entre a view e o modelo do domínio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String password;
}