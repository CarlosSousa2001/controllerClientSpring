package com.client.controllerClienteMvc.web.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PersonCreateDto {

    @NotBlank
    @Size(min = 4 ,max = 85)
    private String name;

    @Size(max = 85)
    @Email(regexp = "/^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+$/")
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 85)
    private String address;

    @Size(max = 400)
    private String text;

    public PersonCreateDto(){}

    public PersonCreateDto(String name, String email, String phone, String address, String text) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
