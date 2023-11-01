package com.client.controllerClienteMvc.repository;

import com.client.controllerClienteMvc.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByUsuarioId(Long id);

    Optional<Person> findFirstByName(String namePerson);
}
