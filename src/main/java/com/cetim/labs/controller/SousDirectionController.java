package com.cetim.labs.controller;

import com.cetim.labs.model.SousDirection;
import com.cetim.labs.dto.ServiceDTO;
import com.cetim.labs.dto.SousDirectionDTO;
import com.cetim.labs.model.Service;
import com.cetim.labs.repository.SousDirectionRepository;
import com.cetim.labs.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/SousDirections")
public class SousDirectionController {

    @Autowired
    private SousDirectionRepository SousDirectionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public List<SousDirectionDTO> getAllSousDirections() {
        List<SousDirection> sousDirections = SousDirectionRepository.findAll();
        return sousDirections.stream()
            .map(sd -> new SousDirectionDTO(
                sd.getId(),
                sd.getName(),
                sd.getService().stream()
                    .map(s -> new ServiceDTO(s.getId(), s.getName()))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }

    @PostMapping
    public SousDirection createSousDirection(@RequestBody SousDirection SousDirection) {
        return SousDirectionRepository.save(SousDirection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousDirection> getSousDirectionById(@PathVariable Long id) {
        return SousDirectionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousDirection> updateSousDirection(@PathVariable Long id, @RequestBody SousDirection SousDirectionDetails) {
        return SousDirectionRepository.findById(id)
                .map(SousDirection -> {
                    SousDirection.setName(SousDirectionDetails.getName());
                    SousDirection.setColor(SousDirectionDetails.getColor());
                    SousDirection.setDescription(SousDirectionDetails.getDescription());
                    return ResponseEntity.ok(SousDirectionRepository.save(SousDirection));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSousDirection(@PathVariable Long id) {
        return SousDirectionRepository.findById(id)
                .map(SousDirection -> {
                    SousDirectionRepository.delete(SousDirection);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/services")
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @PostMapping("/{id}/services")
    @Transactional
    public ResponseEntity<?> addServiceToSousDirection(@PathVariable Long id, @RequestBody Service service) {
        return SousDirectionRepository.findById(id)
                .map(SousDirection -> {
                    service.setSousDirection(SousDirection);
                    Service savedService = serviceRepository.save(service);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}