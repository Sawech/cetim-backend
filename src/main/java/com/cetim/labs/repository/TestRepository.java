package com.cetim.labs.repository;

import com.cetim.labs.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    
    List<Test> findByService(String service);
    boolean existsByTestName(String testName);
    Optional<Test> findByTestCode(String testCode);
    void deleteByTestCode(String id);
    List<Test> findAllByTestCodeIn(List<String> testCodes);
    boolean existsByTestCode(String testID);
    long countByTestCode(String testCode);
}