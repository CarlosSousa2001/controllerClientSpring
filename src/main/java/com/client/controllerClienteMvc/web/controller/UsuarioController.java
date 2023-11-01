package com.client.controllerClienteMvc.web.controller;

import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.service.UsuarioService;
import com.client.controllerClienteMvc.web.dto.UsuarioCreateDto;
import com.client.controllerClienteMvc.web.dto.UsuarioResponseDto;
import com.client.controllerClienteMvc.web.dto.UsuarioSenhaDto;
import com.client.controllerClienteMvc.web.dto.mapper.GenericsMapper;
import com.client.controllerClienteMvc.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Contém todas os operações para entidade usuário")
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário", tags = "Usuários",
            responses = {
                    @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409",description = "Usuario já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto usuarioDto) {
        Usuario user = usuarioService.createUser(GenericsMapper.mapTo(usuarioDto, Usuario.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericsMapper.toDto(user));
    }

    @Operation(summary = "Listar todos usuários", description = "Acesso restrito para ADMIN", tags = "Usuários",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Lista de usuários",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))),
                    @ApiResponse(responseCode = "404",description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> gelAllUsers() {
        List<Usuario> users = usuarioService.findAllUsers();
        return ResponseEntity.ok().body(GenericsMapper.mapListResponse(users));
    }

    @Operation(summary = "Buscar usuário por ID", tags = "Usuários",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Usuário encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403",description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404",description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE')) AND #id == authentication.principal.id")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.findByIdUser(id);
        return ResponseEntity.ok().body(GenericsMapper.toDto(user));
    }
    @Operation(summary = "Atualizar senha", tags = "Usuários",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204",description = "Senha atualizada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
                    @ApiResponse(responseCode = "404",description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404",description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400",description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND (#id == authentication.principal.id) ")
    public ResponseEntity<UsuarioResponseDto> updatePassword(@PathVariable @Valid Long id, @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.updatePasswordUser(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.ok().body(GenericsMapper.toDto(user));
    }

    @Operation(summary = "Deletar usuario ppor id", tags = "Usuários",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204",description = "Usuário deletado",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = void.class)))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        usuarioService.deleteByIdUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário deletado");
    }
}
