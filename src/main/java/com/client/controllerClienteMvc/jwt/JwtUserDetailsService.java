package com.client.controllerClienteMvc.jwt;

import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public JwtUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getByusername(username);
        return new JwtUserDetails(usuario);
    }
    // usando com o cliente for fazer o authenticação - geramos o token.
    public JwtToken getTokenAuthenticated(String username){
        Usuario.Role role = usuarioService.getRoleByUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}
