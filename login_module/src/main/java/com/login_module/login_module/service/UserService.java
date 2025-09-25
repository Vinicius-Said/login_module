package com.login_module.login_module.service;

import com.login_module.login_module.dto.UserDto;
import com.login_module.login_module.model.User;

/**
 * Interface que define o contrato para a lógica de negócio relacionada a usuários.
 */
public interface UserService {
    void saveUser(UserDto userDto);
    User findByUsername(String username);
}