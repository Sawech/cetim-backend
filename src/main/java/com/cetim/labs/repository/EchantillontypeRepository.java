package com.cetim.labs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cetim.labs.model.EchantillonTypes;

public interface EchantillontypeRepository extends JpaRepository<EchantillonTypes, Integer> {

    Optional<EchantillonTypes> findByName(String name);
}
