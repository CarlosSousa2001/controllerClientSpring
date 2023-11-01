package com.client.controllerClienteMvc.service;

import com.client.controllerClienteMvc.entity.Car;
import com.client.controllerClienteMvc.entity.Work;
import com.client.controllerClienteMvc.repository.WorkRepository;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;


    public WorkService(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public Work createWork(Work work){
        return workRepository.save(work);
    }

    @Transactional(readOnly = true)
    public List<Work> getAllWork(){
        return workRepository.findAll();
    }

    public Work getWorkByid(Long id) {
        Optional<Work> workOptional = workRepository.findById(id);

        if(workOptional.isPresent()){
            return workOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized(String.format("Work %s não encontrado", id));
    }

    public Work updateWork(Long id, String name, Integer price, String text) {
        var work = getWorkByid(id);

        work.setName(name);
        work.setPrice(price);
        work.setText(text);

        return workRepository.save(work);
    }

    public void deleteById(Long id) {
        workRepository.deleteById(id);
    }
}
