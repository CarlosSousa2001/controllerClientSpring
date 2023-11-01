package com.client.controllerClienteMvc.service;

import com.client.controllerClienteMvc.entity.Car;
import com.client.controllerClienteMvc.repository.CarRepository;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public Car getCarByPlate(String carPlate) {
        Optional<Car> carOptional = carRepository.findFirstByLicensePlate(carPlate);

        if(carOptional.isPresent()){
            return carOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized(String.format("Placa %s não encontrada", carOptional));
    }

    @Transactional(readOnly = true)
    public Car getCarById(Long id) {
        Optional<Car> carOptional = carRepository.findById(id);

        if(carOptional.isPresent()){
            return carOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized(String.format("Placa %s não encontrada", carOptional));
    }

    public Car updateCar(Long id, String model, String make, Integer year, String licensePlate) {
        Car car = getCarById(id);

        car.setModel(model);
        car.setMake(make);
        car.setYear(year);
        car.setLicensePlate(licensePlate);

        return carRepository.save(car);
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }
}
