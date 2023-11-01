package com.client.controllerClienteMvc.web.controller;

import com.client.controllerClienteMvc.entity.Person;
import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.jwt.JwtUserDetails;
import com.client.controllerClienteMvc.service.PersonService;
import com.client.controllerClienteMvc.service.UsuarioService;
import com.client.controllerClienteMvc.web.dto.PersonCreateDto;
import com.client.controllerClienteMvc.web.dto.mapper.GenericsMapper;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "persons", description = "Contém todos os recursos referentes a entidade person")
@RestController
@RequestMapping("api/v1/persons")
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private UsuarioService usuarioService;


    @Operation(summary = "Criar person", tags = "persons", description = "O recurso para criar um novo person, será vinculado automaticamente ao usuário logado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonCreateDto.class))),
                    @ApiResponse(responseCode = "409", description = "Person já cadastrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Person> create(@RequestBody PersonCreateDto dto,
                                         @AuthenticationPrincipal JwtUserDetails userDetails) {
        Person person = GenericsMapper.mapTo(dto, Person.class);
        person.setUsuario(usuarioService.findByIdUser(userDetails.getId()));
        personService.createPerson(person);
        return ResponseEntity.status(201).body(person);
    }

    @Operation(summary = "Listar todos os persons", tags = "persons", description = "O recurso retorn uma lista com todos os persons caso o perfil seja 'ADMIN' - para o perfil 'CLIENTE' so retorna dados relacionados oa person que esta logado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Person.class)))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<List<Person>> getAllPerson(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<Person> personList;
        if (userDetails.getRole().contains("ROLE_ADMIN")) {
            personList = personService.findAllPerson();
        } else {
            personList = personService.getPersonByUsuarioId(userDetails.getId());
        }
        return ResponseEntity.ok().body(personList);
    }


    @Operation(summary = "Bucar Person por id", tags = "persons", description = "O recurso precisa de um Usuário logado e um id válido",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonCreateDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundExceptionPersonalized.class))),
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok().body(person);
    }

    @Operation(summary = "Atualizar person", tags = "persons",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204",description = "Person Atualizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
                    @ApiResponse(responseCode = "404",description = "Person não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400",description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Person> updatePassword(@PathVariable @Valid Long id, @RequestBody PersonCreateDto dto) {
        Person person = personService.updatePerson(id, dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getText());
        return ResponseEntity.ok().body(person);
    }


    @Operation(summary = "Deletar person por id", tags = "persons",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "person deletado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Person deletado");
    }
}
