package com.cetim.labs.repository;

import com.cetim.labs.model.SousDirection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SousDirectionRepository extends JpaRepository<SousDirection, Long> {
    SousDirection findByName(String name);
} 