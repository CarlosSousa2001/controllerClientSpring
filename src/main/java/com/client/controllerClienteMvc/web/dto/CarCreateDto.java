package com.client.controllerClienteMvc.web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CarCreateDto {

    @NotBlank
    @Size(max = 70)
    private String model;

    @NotBlank
    @Size(max = 70)
    private String make;

    @NotBlank
    @Size(min = 4, max = 4)
    private Integer year;

    @NotBlank
    @Size(min = 4, max = 12)
    private String licensePlate;

    @NotBlank
    @Size(max = 70)
    private String color;

    @NotBlank
    @Size(max = 70)
    private String namePerson;

    public CarCreateDto(){}

    public CarCreateDto(String model, String make, Integer year, String licensePlate, String color, String namePerson) {
        this.model = model;
        this.make = make;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
        this.namePerson = namePerson;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }
}
