package com.client.controllerClienteMvc.repository;

import com.client.controllerClienteMvc.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository  extends JpaRepository<Work, Long> {
}
