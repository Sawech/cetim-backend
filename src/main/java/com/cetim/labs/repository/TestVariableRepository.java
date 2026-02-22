package com.cetim.labs.repository;

import com.cetim.labs.model.TestVariable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestVariableRepository extends JpaRepository<TestVariable, Long> {
}