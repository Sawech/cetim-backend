package com.cetim.labs.controller;

import com.cetim.labs.dto.TestDTO;
import com.cetim.labs.dto.TestElementDTO;
import com.cetim.labs.dto.TestVariableDTO;
import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.Test;
import com.cetim.labs.model.TestElement;
import com.cetim.labs.model.TestVariable;
import com.cetim.labs.repository.ficheDessaiRepository;
import com.cetim.labs.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ficheDessaiRepository ficheDessaitestService;


    @PostMapping
    public ResponseEntity<Test> saveTest(@RequestBody TestDTO testDTO) {
        Test test = new Test();
        test.setService(testDTO.getService());
        test.setTestCode(testDTO.getTestCode());
        test.setTestName(testDTO.getTestName());
        test.setIsPrimaryTest(testDTO.isIsPrimaryTest());
        test.setComplexeTest(testDTO.isComplexeTest()); // Make sure to set this

        if (testDTO.isComplexeTest()) {
            // Handle complex test case - update subTestIds
            test.setSubTestIds(testDTO.getSubTestIds());
            // Clear elements for complex test
            test.setElements(null);
            test.setVariables(null);
        } else {
            // Handle simple test case - update elements
            List<TestElement> elements = testDTO.getElements().stream()
                    .filter(Objects::nonNull) // Filter out null elements
                    .map(dto -> {
                        TestElement element = new TestElement();
                        element.setType(dto.getType());
                        element.setContent(dto.getContent());
                        element.setTableContent(dto.getTableContent());
                        element.setImportant(dto.isImportant());
                        element.setPosition(testDTO.getElements().indexOf(dto));
                        element.setSide(dto.getSide());
                        return element;
                    })
                    .collect(Collectors.toList());

            test.setElements(elements);

            // Handle variables
            List<TestVariable> variables = testDTO.getVariables() == null ? List.of()
                    : testDTO.getVariables().stream()
                            .filter(Objects::nonNull) // Filter out null elements
                            .map(dto -> {
                                TestVariable variable = new TestVariable();
                                variable.setName(dto.getName());
                                variable.setExpression(dto.getExpression());
                                variable.setComputedValue(dto.getComputedValue());
                                variable.setMin(dto.getMin());
                                variable.setMax(dto.getMax());
                                variable.setUnit(dto.getUnit());
                                return variable;
                            })
                            .collect(Collectors.toList());
            test.setVariables(variables);
            // Clear subTestIds for simple test
            test.setSubTestIds(null);
        }
        Test savedTest = testService.saveTest(test);
        return ResponseEntity.ok(savedTest);
    }

    @PostMapping("/complexeTest")
    public ResponseEntity<Test> saveComplexeTest(@RequestBody TestDTO testDTO) {
        Test test = new Test();
        test.setService(testDTO.getService());
        test.setTestCode(testDTO.getTestCode());
        test.setTestName(testDTO.getTestName());
        test.setIsPrimaryTest(testDTO.isIsPrimaryTest());
        test.setSubTestIds(testDTO.getSubTestIds());

        Test savedTest = testService.saveTest(test);
        return ResponseEntity.ok(savedTest);
    }

    // Keep other methods unchanged
    @GetMapping
    public ResponseEntity<List<Test>> getAllTests() {
        List<Test> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/assign/{service}")
    public ResponseEntity<List<Test>> getServiceTests(@PathVariable String service) {
        List<FicheDessai> fEssai = ficheDessaitestService.findByService(service);
        List<Test> tests = fEssai.stream()
                .flatMap(fiche -> fiche.getOrder().getTests().stream())
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        
        Test test = testService.getTestById(id);
        return ResponseEntity.ok(test);
    }

    @GetMapping("/direction/{direction}")
    public ResponseEntity<List<Test>> getTestsService(@PathVariable String direction) {
        List<Test> tests = testService.getTestsByService(direction);
        return ResponseEntity.ok(tests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Long id, @RequestBody TestDTO testDTO) {
        Test test = new Test();
        test.setService(testDTO.getService());
        test.setTestCode(testDTO.getTestCode());
        test.setTestName(testDTO.getTestName());
        test.setIsPrimaryTest(testDTO.isIsPrimaryTest());
        test.setComplexeTest(testDTO.isComplexeTest()); // Make sure to set this

        if (testDTO.isComplexeTest()) {
            test.setSubTestIds(testDTO.getSubTestIds());
            test.setElements(null); // Explicitly set to null for complex tests
            test.setVariables(null);
        } else {
            // Handle simple test case - update elements
            List<TestElement> elements = IntStream.range(0, testDTO.getElements().size())
                            .filter(Objects::nonNull) // Filter out null elements
                    .mapToObj(i -> {
                        TestElementDTO dto = testDTO.getElements().get(i);
                        TestElement element = new TestElement();
                        element.setId(dto.getId());
                        element.setType(dto.getType());
                        element.setContent(dto.getContent());
                        element.setTableContent(dto.getTableContent());
                        element.setPosition(i);
                        element.setImportant(dto.isImportant());
                        element.setSide(dto.getSide());
                        return element;
                    })
                    .collect(Collectors.toList());
            test.setElements(elements);

            // Handle variables
            List<TestVariable> variables = testDTO.getVariables().stream()
                            .filter(Objects::nonNull) // Filter out null elements
                    .map(dto -> {
                        TestVariable variable = new TestVariable();
                        variable.setId(dto.getId());
                        variable.setName(dto.getName());
                        variable.setExpression(dto.getExpression());
                        variable.setComputedValue(dto.getComputedValue());
                        variable.setMin(dto.getMin());
                        variable.setMax(dto.getMax());
                        variable.setUnit(dto.getUnit());
                        return variable;
                    })
                    .collect(Collectors.toList());
            test.setVariables(variables);
            // Clear subTestIds for simple test
            test.setSubTestIds(null);
        }
        Test updatedTest = testService.updateTest(id, test);
        return ResponseEntity.ok(updatedTest);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @PostMapping("/variable")
    public ResponseEntity<TestVariable> saveVariable(
            @RequestBody Map<String, Object> requestBody) {
        try {
            Long testId = Long.parseLong(requestBody.get("testId").toString());
            Long variableId = Long.parseLong(requestBody.get("variableId").toString());
            Double computedValue = Double.parseDouble(requestBody.get("computedValue").toString());
            String expression = requestBody.get("computedValue").toString();

            TestVariable newVariable = testService.saveVariable(testId, variableId, computedValue, expression);
            return ResponseEntity.ok(newVariable);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-code")
    public ResponseEntity<Boolean> checkTestCodeUnique(
            @RequestParam String code) {

        boolean isUnique = testService.isTestCodeUnique(code);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping(value = "/variables/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToVariableUpdates() {
        SseEmitter emitter = new SseEmitter(3600000L); // 1 hour timeout
        // Store the emitter somewhere (you'll need an emitter registry)
        TestService.subscribeEmitter(emitter);
        return emitter;
    }

    
    @GetMapping(value = "/variables/saves", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToElementSaves() {
        SseEmitter emitter = new SseEmitter(3600000L); // 1 hour timeout
        // Store the emitter somewhere (you'll need an emitter registry)
        TestService.subscribeEmitter(emitter);
        return emitter;
    }

    @PutMapping("/variables/{id}")
    public ResponseEntity<TestVariable> updateTestVariable(
            @PathVariable Long id,
            @RequestBody TestVariableDTO variableDTO) {
        try {
            TestVariable variable = new TestVariable();
            variable.setId(id);
            variable.setExpression(variableDTO.getExpression());
            variable.setComputedValue(variableDTO.getComputedValue());

            TestVariable updatedVariable = testService.updateVariable(variable);

            // Broadcast the update to all subscribers
            TestService.broadcastUpdate(updatedVariable);
            return ResponseEntity.ok(updatedVariable);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}