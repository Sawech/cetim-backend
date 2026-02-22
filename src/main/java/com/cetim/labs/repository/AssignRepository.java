package com.cetim.labs.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cetim.labs.model.Assign;

public interface AssignRepository extends JpaRepository<Assign, Long> {
    List<Assign> findByUserId(Long userId);
}
