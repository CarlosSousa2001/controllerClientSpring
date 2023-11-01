package com.client.controllerClienteMvc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WorkCreateDto {

    @NotBlank
    @Size(max = 70)
    private String name;

    @NotBlank
    @Size(max = 70)
    private Integer price;

    @NotBlank
    @Size(max = 400)
    private String text;

    @NotBlank
    private String carPlate;

    public WorkCreateDto(){}

    public WorkCreateDto(String name, Integer price, String text, String carPlate) {
        this.name = name;
        this.price = price;
        this.text = text;
        this.carPlate = carPlate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }
}
