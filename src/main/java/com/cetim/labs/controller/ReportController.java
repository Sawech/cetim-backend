package com.cetim.labs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cetim.labs.dto.FicheDessaiDTO;
import com.cetim.labs.dto.RapportDessaiRequestDTO;
import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.RapportEssai;
import com.cetim.labs.repository.ficheDessaiRepository;
import com.cetim.labs.service.RapportEssaiService;
import com.cetim.labs.service.rapportpartielService;
import com.cetim.labs.model.RapportFinal;
import com.cetim.labs.model.RapportPartiel;
import com.cetim.labs.model.Service;
import com.cetim.labs.model.SousDirection;
import com.cetim.labs.model.Test;
import com.cetim.labs.repository.RapportEssaiRepository;
import com.cetim.labs.repository.RapportFinalRepository;
import com.cetim.labs.repository.RapportPartielRepository;
import com.cetim.labs.repository.SousDirectionRepository;
import com.cetim.labs.repository.TestRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fichedessai")
public class ReportController {

    @Autowired
    private RapportFinalRepository raportFinalRepo;

    @Autowired
    private ficheDessaiRepository ficheDessaiRepository;

    @Autowired
    private SousDirectionRepository sousDirectionRepository;

    @Autowired 
    private RapportPartielRepository RPripository;

    @Autowired 
    private RapportEssaiRepository RERipository;

    @Autowired
    private TestRepository testRepository;

    @Autowired 
    private RapportEssaiService rapportEssaiService;

    @Autowired
    private rapportpartielService rapportPartielService;


    @GetMapping("/{raportFinalId}")
    public ResponseEntity<?> getFicheDessaiByRaportFinalId(@PathVariable int raportFinalId) {
        RapportFinal raportFinal = raportFinalRepo.findById(raportFinalId).orElse(null);
        if (raportFinal == null) {
            return ResponseEntity.status(404).body("RapportFinal not found with ID: " + raportFinalId);
        }
        var os = raportFinal.getOS();
        if (os == null) {
            return ResponseEntity.status(404).body("OS not found for RapportFinal ID: " + raportFinalId);
        }
        List<FicheDessai> fichesDessais = ficheDessaiRepository.findByServiceOrder(os);
        List<FicheDessaiDTO> ficheDessaiDTOs = fichesDessais.stream()
            .map(ficheDessai -> new FicheDessaiDTO(
                ficheDessai.getId(),
                raportFinal.getId(),
                os.getServiceOrderID(),
                os.getDate(),
                ficheDessai.getOrder()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(ficheDessaiDTOs);
    }
    
    @GetMapping
    public ResponseEntity<List<FicheDessaiDTO>> getFichesDessais() {
        List<FicheDessai> fichesDessais = ficheDessaiRepository.findAll();
        List<FicheDessaiDTO> ficheDessaiDTOs = fichesDessais.stream()
            .map(ficheDessai -> new FicheDessaiDTO(
                ficheDessai.getId(),
                ficheDessai.getServiceOrder().getRaportFinal().getId(),
                ficheDessai.getServiceOrder().getServiceOrderID(),
                ficheDessai.getServiceOrder().getDate(),
                ficheDessai.getOrder()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(ficheDessaiDTOs);
    }

    @GetMapping("/SousDirection/{SousDirection}")
    public ResponseEntity<List<FicheDessaiDTO>> getFichesBySousDirection(@PathVariable String SousDirection) {
        SousDirection sousDirection = sousDirectionRepository.findByName(SousDirection);
        if (sousDirection == null) {
            throw new IllegalArgumentException("SousDirection not found with name: " + SousDirection);
        }
        List<String> serviceNames = sousDirection.getService().stream()
                                            .map(Service::getName)
                                            .collect(Collectors.toList());
        List<FicheDessai> fe = ficheDessaiRepository.findByTestSousDirection(serviceNames);
         List<FicheDessaiDTO> ficheDessaiDTOs = fe.stream()
            .map(ficheDessai -> new FicheDessaiDTO(
                ficheDessai.getId(),
                ficheDessai.getServiceOrder().getRaportFinal().getId(),
                ficheDessai.getServiceOrder().getServiceOrderID(),
                ficheDessai.getServiceOrder().getDate(),
                ficheDessai.getOrder()
            ))
            .collect(Collectors.toList());

            return ResponseEntity.ok(ficheDessaiDTOs);
    }

    @GetMapping("/details/{ficheDessaiId}")
    public ResponseEntity<?> getFicheDessaiDetails(@PathVariable int ficheDessaiId) {
        FicheDessai ficheDessai = ficheDessaiRepository.findById(ficheDessaiId).orElse(null);
        if (ficheDessai == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FicheDessai not found with ID: " + ficheDessaiId);
        }

        RapportPartiel rapportPartiel = ficheDessai.getRaportPartiel();
        if (rapportPartiel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RapportPartiel not found for FicheDessai ID: " + ficheDessaiId);
        }

        RapportFinal rapportFinal = rapportPartiel.getRapportFinal();
        if (rapportFinal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RapportFinal not found for FicheDessai ID: " + ficheDessaiId);
        }

        // Make sure FicheDessaiDTO has a constructor with these parameters
        FicheDessaiDTO dto = new FicheDessaiDTO(
            ficheDessaiId,
            rapportFinal.getId(),
            rapportPartiel.getId(),
            rapportFinal.getOS().getDate(),
            ficheDessai.getOrder()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFicheDessei(@PathVariable int id) {
        ficheDessaiRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/Rapportdessai")
    public ResponseEntity<?> createRapportDessai(@RequestBody RapportDessaiRequestDTO request) {
        try {
            RapportEssai rapportEssai = new RapportEssai();
            FicheDessai ficheDessai = ficheDessaiRepository.findById(request.getFicheDessaiId())
                    .orElseThrow(() -> new IllegalArgumentException("FicheDessai not found with ID: " + request.getFicheDessaiId()));

            Test test = testRepository.findById(request.getTestId())
                    .orElseThrow(() -> new IllegalArgumentException("Test not found with ID: " + request.getTestId()));

            rapportEssai.setTest(test);
            RapportPartiel rappar = ficheDessai.getRaportPartiel();
            rappar.addRapportEssai(rapportEssai);
            rapportEssaiService.generateDocument(rapportEssai);


    
            return ResponseEntity.ok("Rapport d'essai créé avec succès!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création du rapport d'essai: " + e.getMessage());
        }
    }

    @PostMapping("/RapportPartiel")
    public ResponseEntity<?> createRapportPartiel(@RequestBody int ficheDessaiId) {
        try {
            FicheDessai ficheDessai = ficheDessaiRepository.findById(ficheDessaiId)
                    .orElseThrow(() -> new IllegalArgumentException("FicheDessai not found with ID: " + ficheDessaiId));

            RapportPartiel rapportPartiel = ficheDessai.getRaportPartiel();
            
            rapportPartielService.generateDocument(rapportPartiel);

            return ResponseEntity.ok("Rapport partiel créé avec succès!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création du rapport partiel: " + e.getMessage());
        }
    }

    
    
}

