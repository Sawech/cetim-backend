package com.cetim.labs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cetim.labs.dto.OrderDTO;
import com.cetim.labs.dto.ServiceOrderDTO;
import com.cetim.labs.dto.TestDTO;
import com.cetim.labs.model.Echantillon;
import com.cetim.labs.model.EchantillonTypes;
import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.Order;
import com.cetim.labs.model.RapportFinal;
import com.cetim.labs.model.RapportPartiel;
import com.cetim.labs.model.SStatus;
import com.cetim.labs.model.ServiceOrder;
import com.cetim.labs.model.Test;
import com.cetim.labs.repository.EchantillontypeRepository;
import com.cetim.labs.repository.OSRepository;
import com.cetim.labs.repository.TestRepository;
import com.cetim.labs.repository.ficheDessaiRepository;
import com.cetim.labs.service.DocumentGenerationService;

import jakarta.persistence.EntityNotFoundException;

import com.cetim.labs.repository.RapportFinalRepository;
import com.cetim.labs.repository.RapportPartielRepository;

@RestController
@RequestMapping("/api/service")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OSController {

	@Autowired
    private OSRepository osRepository;
	
	@Autowired
    private TestRepository testRepository;

    @Autowired
    private EchantillontypeRepository ETRepository;

    @Autowired
    private ficheDessaiRepository ficheDessaiRepository;

    @Autowired
    private RapportFinalRepository RaportFinalRepository;

    @Autowired
    private DocumentGenerationService documentGenerationService;

    @Autowired
    private RapportPartielRepository rapportPartielRepository;
    

    @PostMapping("/createOS")
    @Transactional
    public ResponseEntity<?> createNewOS(@RequestBody ServiceOrderDTO serviceOrderDTO) {
        try {
            ServiceOrder serviceOrder = new ServiceOrder();
            serviceOrder.setDate(serviceOrderDTO.getDate());
            serviceOrder.setOrders(new ArrayList<>());

            for (OrderDTO orderDTO : serviceOrderDTO.getOrders()) {
                Echantillon echantillon = new Echantillon();
                echantillon.setEchantillonCode(orderDTO.getEchantillonID());

                EchantillonTypes ET = ETRepository.findByName(orderDTO.getEchantillonType())
                    .orElseThrow(() -> new EntityNotFoundException("EchantillonType not found"));

                echantillon.setEchantillonType(ET);
                echantillon.setDate(new Date());
                
                Order order = new Order();
                order.setEchantillon(echantillon);
                order.setDelai(orderDTO.getDelai());
                order.setServiceOrder(serviceOrder);

                if (orderDTO.getTests() != null && !orderDTO.getTests().isEmpty()) {
                    Set<Test> allTests = new HashSet<>(testRepository.findAllByTestCodeIn(orderDTO.getTests()));
                    Set<Test> primaryTests = allTests.stream()
                        .filter(Test::isIsPrimaryTest)
                        .collect(Collectors.toSet());
                    order.setTests(primaryTests);
                }

                serviceOrder.getOrders().add(order);
            }

            ServiceOrder savedOS = osRepository.save(serviceOrder);

            // Generate the document for the new OS
            documentGenerationService.generateDocument(savedOS, null);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating OS: " + e.getMessage());
        }
    }

    @GetMapping("/os/{id}")
    public ResponseEntity<?> getOS(@PathVariable int id) {
        try {
            ServiceOrder os = osRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS not found"));
            
            ServiceOrderDTO osDTO = new ServiceOrderDTO();
            osDTO.setOSID(id);

            if (os.getRaportFinal() != null) {
                osDTO.setRaportFinalRE(os.getRaportFinal().getId());
                
            }
            osDTO.setDate(os.getDate());
            osDTO.setStatus(os.getStatus());
            
            for (Order order : os.getOrders()) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderID(order.getOrderID());
                orderDTO.setEchantillonID(order.getEchantillon().getEchantillonCode());
                orderDTO.setDelai(order.getDelai());

                for (Test test : order.getTests()) {
                    TestDTO testDTO = new TestDTO();
                    testDTO.setTestCode(test.getTestCode());
                    testDTO.setTestName(test.getTestName());
                    testDTO.setTestCode(test.getTestCode());
                    testDTO.setService(test.getService());
                    testDTO.setIsPrimaryTest(test.isIsPrimaryTest());

                    orderDTO.addTestDTO(testDTO);
                }
                
                osDTO.addOrder(orderDTO);
            }
            
            return ResponseEntity.ok(osDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("OS not found: " + e.getMessage());
        }
    }

    @GetMapping("/os")
    public ResponseEntity<?> getAllOS() {
        try {
            List<ServiceOrder> osList = osRepository.findAll();
            List<ServiceOrderDTO> osDTOList = new ArrayList<>();
            for (ServiceOrder os : osList) {
                if (os != null) {
                    ServiceOrderDTO osDTO = new ServiceOrderDTO();
                    osDTO.setOSID(os.getServiceOrderID());
                    osDTO.setRaportFinalRE(os.getRaportFinal() != null ? os.getRaportFinal().getId() : null);
                    osDTO.setDate(os.getDate());
                    osDTO.setStatus(os.getStatus());
                    for (Order order : os.getOrders()) {
                        OrderDTO orderDTO = new OrderDTO();
                        orderDTO.setOrderID(order.getOrderID());
                        orderDTO.setEchantillonID(order.getEchantillon().getEchantillonCode());
                        orderDTO.setDelai(order.getDelai());
                        
                        osDTO.addOrder(orderDTO);
                    }

                    osDTOList.add(osDTO);

                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("OS not found");
                }
            }
            return ResponseEntity.ok(osDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching OS list: " + e.getMessage());
        }
    }

    @DeleteMapping("/os/{id}")
    @Transactional
    public ResponseEntity<?> deleteOS(@PathVariable int id) {
        try {
            // Check if the OS exists
            Optional<ServiceOrder> osOptional = osRepository.findById(id);
            if (!osOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("OS not found with ID: " + id);
            }
            
            ServiceOrder os = osOptional.get();
            
            // Clear the tests from each order before deletion
            for (Order order : os.getOrders()) {
                order.setTests(new HashSet<>());
            }
            
            // Delete the OS (this will cascade delete the orders)
            osRepository.delete(os);
            
            return ResponseEntity.ok("OS deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting OS: " + e.getMessage());
        }
    }

    @PutMapping("/os/{id}/accept")
    @Transactional
    public ResponseEntity<?> acceptOS(@PathVariable int id) {
        try {
            ServiceOrder os = osRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS not found"));

  
            if (os.getOrders() == null || os.getOrders().isEmpty()) {
                return ResponseEntity.badRequest().body("OS has no orders");
            }

            RapportFinal rapportFinal = new RapportFinal();
            rapportFinal.setOS(os);
            os.setRE(rapportFinal);
            rapportFinal = RaportFinalRepository.save(rapportFinal); 

            for (Order order : os.getOrders()) {
                RapportPartiel rapportPartiel = new RapportPartiel();
                
                FicheDessai ficheDessai = new FicheDessai();
                ficheDessai.setOrder(order);
                ficheDessai.setCreationDate(new Date());
                ficheDessai.setStatu(SStatus.NOUVEAU);
                ficheDessai.setServiceOrder(os);
                
                ficheDessai.setRaportPartiel(rapportPartiel);
                rapportPartiel.setFicheEssai(ficheDessai);
                
                rapportPartiel.setRapportFinal(rapportFinal);
                
                ficheDessai = ficheDessaiRepository.save(ficheDessai);
                rapportPartielRepository.save(rapportPartiel);
                
                documentGenerationService.generateDocument(null, ficheDessai);
                
                os.addFicheDessai(ficheDessai);
            }

            os.setStatus(SStatus.ACCEPTE);

            return ResponseEntity.ok("OS accepted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error accepting OS: " + e.getMessage());
        }
    }

    @PutMapping("/os/{id}/reject")
    @Transactional
    public ResponseEntity<?> rejectOS(@PathVariable int id) {
        try {
            ServiceOrder os = osRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS not found"));
            os.setStatus(SStatus.REJETE);
            osRepository.save(os);
            return ResponseEntity.ok("OS rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error rejecting OS: " + e.getMessage());
        }
    }

    @PutMapping("/os/{id}/statu")
    @Transactional
    public ResponseEntity<?> StatuHandling(@PathVariable int id, @RequestBody SStatus status) {
        try {
            ServiceOrder os = osRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS not found"));
            os.setStatus(status);
            osRepository.save(os);
            return ResponseEntity.ok("OS rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error rejecting OS: " + e.getMessage());
        }
    }

    
}