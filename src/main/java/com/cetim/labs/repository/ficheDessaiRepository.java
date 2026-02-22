package com.cetim.labs.repository;

import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.ServiceOrder;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ficheDessaiRepository extends JpaRepository<FicheDessai, Integer> {
    @Query(value = "SELECT DISTINCT fs.* FROM fiche_dessai fs " +
                  "JOIN orders o ON fs.order_id = o.order_id " +
                  "JOIN order_tests ot ON o.order_id = ot.order_id " +
                  "JOIN test t ON ot.test_id = t.id " +
                  "WHERE t.service in :services", 
          nativeQuery = true)
    List<FicheDessai> findByTestSousDirection(@Param("services") List<String> services);

    @Query(value = "SELECT DISTINCT fs.* FROM fiche_dessai fs " +
                  "JOIN orders o ON fs.order_id = o.order_id " +
                  "JOIN order_tests ot ON o.order_id = ot.order_id " +
                  "JOIN test t ON ot.test_id = t.id " +
                  "WHERE t.service like :services", 
          nativeQuery = true)
    List<FicheDessai> findByService(@Param("services") String services);

    List<FicheDessai> findByServiceOrder(ServiceOrder os);

} 
