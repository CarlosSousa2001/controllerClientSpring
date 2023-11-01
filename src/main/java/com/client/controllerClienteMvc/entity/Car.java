package com.client.controllerClienteMvc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "tb_cars")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "model", nullable = false, length = 70)
    private String model;

    @Column(name = "make", nullable = false, length = 70)
    private String make;

    @Column(name = "year", nullable = false, length = 4)
    private Integer year;

    @Column(name = "license_plate", nullable = false, unique = true, length = 12)
    private String licensePlate;

    @Column(name = "color", length = 20)
    private String color;

    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "cars")
    private Set<Person> person = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "car_works",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id"))
    private List<Work> works;

    public Car() {
    }

    public Car(Long id, String model, String make, Integer year, String licensePlate, String color, Date registrationDate) {
        this.id = id;
        this.model = model;
        this.make = make;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Person> getPerson() {
        return person;
    }

    public List<Work> getWorks() {
        return works;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
