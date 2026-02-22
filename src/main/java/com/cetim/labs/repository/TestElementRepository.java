package com.cetim.labs.repository;

import com.cetim.labs.model.TestElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestElementRepository extends JpaRepository<TestElement, Long> {
}