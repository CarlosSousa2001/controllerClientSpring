package com.client.controllerClienteMvc.web.controller;

import com.client.controllerClienteMvc.entity.Car;
import com.client.controllerClienteMvc.entity.Work;
import com.client.controllerClienteMvc.service.CarService;
import com.client.controllerClienteMvc.service.WorkService;
import com.client.controllerClienteMvc.web.dto.PersonCreateDto;
import com.client.controllerClienteMvc.web.dto.WorkCreateDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "works", description = "Contém todos os recursos refrentes a entidade Work")
@RestController
@RequestMapping("api/v1/works")
public class WorkController {

    @Autowired
    private WorkService workService;

    @Autowired
    private CarService carService;

    @Operation(summary = "Criar um work", tags = "works", description = "O recurso para criar um novo work, precisa de um carro para ser associado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Work.class))),
                    @ApiResponse(responseCode = "409", description = "Person já cadastrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Work> create(@RequestBody WorkCreateDto dto){
        Car car = carService.getCarByPlate(dto.getCarPlate());

        Work work = GenericsMapper.mapTo(dto, Work.class);

        work.getCars().add(car);

        car.getWorks().add(work);

        workService.createWork(work);

        return ResponseEntity.status(HttpStatus.CREATED).body(work);
    }

    @Operation(summary = "Listar todos os works", tags = "works", description = "O recurso retorna uma lista com todos os works ",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Work.class)))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<List<Work>> getAllworks(){

        List<Work> workList  = workService.getAllWork();

        return ResponseEntity.ok().body(workList);
    }

    @Operation(summary = "Bucar Work por id", tags = "works", description = "O recurso precisa de um usuário logado e um id válido",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Work.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundExceptionPersonalized.class))),
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Work> getById(@PathVariable Long id){
        var work = workService.getWorkByid(id);
        return ResponseEntity.ok().body(work);
    }
    @Operation(summary = "Atualizar Work", tags = "works",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204",description = "Car Atualizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
                    @ApiResponse(responseCode = "404",description = "Person não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400",description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            }
    )
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Work> updateWork(@PathVariable Long id, @RequestBody WorkCreateDto dto){
        var work = workService.updateWork(id, dto.getName(), dto.getPrice(), dto.getText());
        return ResponseEntity.ok().body(work);
    }

    @Operation(summary = "Deletar car por id", tags = "works",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "person deletado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<?> deleteCar(@PathVariable Long id){
        workService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Car deletado");
    }
}
