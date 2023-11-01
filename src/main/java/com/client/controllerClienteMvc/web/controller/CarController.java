package com.client.controllerClienteMvc.web.controller;

import com.client.controllerClienteMvc.entity.Car;
import com.client.controllerClienteMvc.entity.Person;
import com.client.controllerClienteMvc.service.CarService;
import com.client.controllerClienteMvc.service.PersonService;
import com.client.controllerClienteMvc.web.dto.CarCreateDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "cars", description = "Contém todoas as operações para entidade Car")
@RestController
@RequestMapping("api/v1/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private PersonService personService;


    @Operation(summary = "Criar um novo Car", tags = "cars", description = "Recurso para criar um novo car", security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
                    @ApiResponse(responseCode = "409", description = "Car já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Car> createCar(@RequestBody CarCreateDto dto) {

        Person person = personService.getPersonByUsername(dto.getNamePerson());

        Car car = GenericsMapper.mapTo(dto, Car.class);

        car.getPerson().add(person);

        person.getCars().add(car);

        carService.createCar(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @Operation(summary = "Listar todos os cars", tags = "cars", description = "O recurso retorna uma lista com todos os cars ",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Car.class)))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> carList = carService.findAllCars();
        return ResponseEntity.ok().body(carList);
    }
    @Operation(summary = "Bucar Car por id", tags = "cars", description = "O recurso precisa de um usuário logado e um id válido",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundExceptionPersonalized.class))),
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Car> getById(@PathVariable Long id){
        var car = carService.getCarById(id);
        return ResponseEntity.ok().body(car);
    }

    @Operation(summary = "Bucar Car pela placa", tags = "cars", description = "O recurso precisa de um usuário logado e um id válido",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundExceptionPersonalized.class))),
            }
    )
    @GetMapping("/{licensePLate}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<Car> getCarByPlate(@PathVariable String plate){
        var car = carService.getCarByPlate(plate);
        return ResponseEntity.ok().body(car);
    }

    @Operation(summary = "Atualizar Car", tags = "cars",
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
    public ResponseEntity<Car> updateCar(@PathVariable Long id, CarCreateDto dto){
        var car = carService.updateCar(id, dto.getModel(), dto.getMake(), dto.getYear(), dto.getLicensePlate());
        return ResponseEntity.ok().body(car);
    }

    @Operation(summary = "Deletar car por id", tags = "cars",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "person deletado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<?> deleteCar(@PathVariable Long id){
        carService.deleteCarById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Car deletado");
    }
}
