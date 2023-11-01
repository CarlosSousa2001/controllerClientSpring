package com.client.controllerClienteMvc.service;

import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.repository.UsuarioRepository;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
import com.client.controllerClienteMvc.web.exception.UsernameUniqueViolationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario createUser(Usuario usuario) {
        try{
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException("Usuário já cadastrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAllUsers() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findByIdUser(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized("Usuário com id "+ id +" não encontrado");
    }

    @Transactional
    public Usuario updatePasswordUser(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        Usuario user = findByIdUser(id);

        if (!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("Senhas não conferem");
        }

        if (!passwordEncoder.matches(senhaAtual, user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;

    }

    @Transactional
    public void deleteByIdUser(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Usuario getByusername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundExceptionPersonalized("Usuário com username "+ username +" não encontrado")
        );
    }

    public Usuario.Role getRoleByUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
