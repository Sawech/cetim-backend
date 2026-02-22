package com.cetim.labs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cetim.labs.model.EchantillonTypes;
import com.cetim.labs.repository.EchantillontypeRepository;

@RestController
@RequestMapping("/api/Echantillon")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class EchantillonController {

    @Autowired
    private EchantillontypeRepository echantillontypeRepository;

    @PostMapping("/types")
    public ResponseEntity<EchantillonTypes> addEchantillonType(@RequestBody EchantillonTypes type) {
        EchantillonTypes savedType = echantillontypeRepository.save(type);
        return new ResponseEntity<>(savedType, HttpStatus.CREATED);
    }

    @GetMapping("/types")
    public ResponseEntity<Iterable<EchantillonTypes>> getAllEchantillonTypes() {
        Iterable<EchantillonTypes> types = echantillontypeRepository.findAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    
} 