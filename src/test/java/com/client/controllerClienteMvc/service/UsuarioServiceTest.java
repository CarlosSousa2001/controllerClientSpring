package com.client.controllerClienteMvc.service;

import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.repository.UsuarioRepository;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @Test
    @DisplayName("usuário criado com sucesso")
    void createUserOk() {
        Usuario usuario = new Usuario();
        usuario.setUsername("teste@gmail.com");
        usuario.setPassword("teste1234");

        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioSalvo = usuarioRepository.findByUsername("teste@gmail.com");
        assertNotNull(usuarioSalvo);
        assertEquals("teste@gmail.com", usuarioSalvo.get().getUsername());
    }

    @Test
    @DisplayName("Campos inválidos")
    void createUserInvalid() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testeEmail.com");
        usuario.setPassword("teste1234");
        try {
            usuarioService.createUser(usuario);
        } catch (Exception ex) {
            Optional<Usuario> usuarioSalvo = usuarioRepository.findByUsername("testeEmail.com");
            assertNull(usuarioSalvo.get());
        }

    }

    @Test
    void findAllUsers() {
    }

    @Test
    @DisplayName("Usuário encontrado com sucesso")
    void findByIdUserExist() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testeid@gmail.com");
        usuario.setPassword("teste1234");

        usuarioRepository.save(usuario);

        Usuario usuarioId = usuarioService.findByIdUser(usuario.getId());

        assertNotNull(usuarioId);
        assertEquals("testeid@gmail.com", usuarioId.getUsername());
    }

    @Test
    @DisplayName("Usuário não encontrado")
    void findByIdUserNotExist() {
        // No lugar do try-catch, você pode usar asserções para verificar se a exceção é lançada.
        Assertions.assertThrows(EntityNotFoundExceptionPersonalized.class, () -> {
            usuarioService.findByIdUser(500L); // Tente encontrar um usuário que não existe
        });
    }

    @Test
    void updatePasswordUser() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testeupdate@gmail.com");
        usuario.setPassword("teste1234");


        String senhaAtual = "teste1234";
        String novaSenha = "testeJunit123";
        String confirmaSenha = "testeJunit123";

        usuarioRepository.save(usuario);

        Usuario updatePassword = usuarioService.updatePasswordUser(usuario.getId(), senhaAtual, novaSenha, confirmaSenha);

        assertNotNull(updatePassword);
        assertEquals(novaSenha, updatePassword.getPassword());

    }
}