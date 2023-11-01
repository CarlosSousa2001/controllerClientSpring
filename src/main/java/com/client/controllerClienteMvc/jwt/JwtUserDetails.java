package com.client.controllerClienteMvc.jwt;

import com.client.controllerClienteMvc.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    private Usuario usuario;

    // na sessão do spring eu preciso add um objeto de tipo user que é um userDetails
    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public Long getId(){
        return this.usuario.getId();
    }
    public String getRole(){
        return this.usuario.getRole().name();
    }
}
