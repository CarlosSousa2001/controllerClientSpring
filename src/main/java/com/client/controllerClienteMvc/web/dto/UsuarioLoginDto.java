package com.client.controllerClienteMvc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioLoginDto {

    @NotBlank(message = "O campo username não pode estar em branco")
    @Size(min = 4, max = 40, message = "O campo username deve ter entre 4 e 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "O campo username deve ser um endereço de e-mail válido")
    private String username;

    @NotBlank(message = "O campo password não pode estar em branco")
    @Size(min = 6, message = "O campo password deve ter pelo menos 6 caracteres")
    private String password;

    public UsuarioLoginDto(){

    }

    public UsuarioLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
