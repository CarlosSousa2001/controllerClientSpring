package com.client.controllerClienteMvc.service;

import com.client.controllerClienteMvc.entity.Person;
import com.client.controllerClienteMvc.repository.PersonRepository;
import com.client.controllerClienteMvc.web.exception.EntityNotFoundExceptionPersonalized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    public List<Person> findAllPerson() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Person> getPersonByUsuarioId(Long id) {
        List<Person> personList = personRepository.findByUsuarioId(id);
        return personList;
    }

    @Transactional(readOnly = true)
    public Person getPersonById(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            return personOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized("Person com id " + id + " não encontrado");
    }

    public Person getPersonByUsername(String namePerson) {
        Optional<Person> personOptional = personRepository.findFirstByName(namePerson);
        if(personOptional.isPresent()){
            return personOptional.get();
        }
        throw new EntityNotFoundExceptionPersonalized("Person não encontrado");
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    public Person updatePerson(Long id, String name, String email, String phone, String address, String text) {
        Person person = getPersonById(id);

        person.setName(name);
        person.setEmail(email);
        person.setPhone(phone);
        person.setAddress(address);
        person.setText(text);

        return personRepository.save(person);
    }
}
