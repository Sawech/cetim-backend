package com.cetim.labs.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cetim.labs.model.ServiceOrder;

import ch.qos.logback.core.status.Status;

@Repository
public interface OSRepository extends JpaRepository<ServiceOrder, Integer> {
	
	@Query("SELECT os FROM ServiceOrder os LEFT JOIN FETCH os.orders o LEFT JOIN FETCH o.tests WHERE os.serviceOrderID = :id")
	Optional<ServiceOrder> findWithOrdersAndTestsByserviceOrderID(@Param("id") String id);
	
	List<ServiceOrder> findByStatus(Status status);
}