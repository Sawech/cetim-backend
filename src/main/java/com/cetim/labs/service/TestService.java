package com.cetim.labs.service;

import com.cetim.labs.model.Test;
import com.cetim.labs.model.TestVariable;
import com.cetim.labs.repository.TestRepository;
import com.cetim.labs.repository.TestVariableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestVariableRepository testVariableRepository;

    private static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public Test saveTest(Test test) {
        if (test.getElements() != null) {
            test.getElements().forEach(element -> element.setTest(test));
        }
        if (test.getVariables() != null) {
            test.getVariables().forEach(variable -> variable.setTest(test));
        }
        return testRepository.save(test);
    }

    public Test getTestById(Long id) {
        return testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
    }

    public List<Test> getTestsByService(String service) {
        return testRepository.findByService(service);
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Test updateTest(Long id, Test incomingTest) {
        Test existingTest = getTestById(id);
        existingTest.setService(incomingTest.getService());
        existingTest.setTestCode(incomingTest.getTestCode());
        existingTest.setTestName(incomingTest.getTestName());
        existingTest.setIsPrimaryTest(incomingTest.isIsPrimaryTest());
        existingTest.setComplexeTest(incomingTest.isComplexeTest());
        existingTest.setSubTestIds(incomingTest.getSubTestIds());

        if (!incomingTest.isComplexeTest()) {
            // Handle simple test case - update elements
            if (existingTest.getElements() == null) {
                existingTest.setElements(new ArrayList<>());
            } else {
                // Instead of clear(), remove all elements through the collection
                existingTest.getElements().removeIf(e -> true); // This maintains the collection reference
            }

            if (incomingTest.getElements() != null) {
                incomingTest.getElements().forEach(element -> {
                    element.setTest(existingTest);
                    existingTest.getElements().add(element);
                });
            }

            if (existingTest.getVariables() == null) {
                existingTest.setVariables(new ArrayList<>());
            } else {
                // Instead of clear(), remove all elements through the collection
                existingTest.getVariables().removeIf(e -> true); // This maintains the collection reference
            }

            if (incomingTest.getVariables() != null) {
                incomingTest.getVariables().forEach(variable -> {
                    variable.setTest(existingTest);
                    existingTest.getVariables().add(variable);
                });
            }
        } else {
            // Handle complex test case - clear elements through the collection
            if (existingTest.getElements() != null) {
                existingTest.getElements().clear();
            }
            if (existingTest.getVariables() != null) {
                existingTest.getVariables().clear();
            }
        }

        return testRepository.save(existingTest);
    }

    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    public boolean isTestCodeUnique(String testCode) {
        if (testCode == null || testCode.trim().isEmpty()) {
            return false;
        }
        return testRepository.countByTestCode(testCode) == 0;
    }

    public TestVariable updateVariable(TestVariable variable) {
        TestVariable existingVariable = testVariableRepository.findById(variable.getId())
                .orElseThrow(() -> new RuntimeException("Variable not found"));

        // Update only the fields that should be updated
        existingVariable.setExpression(variable.getExpression());
        existingVariable.setComputedValue(variable.getComputedValue());

        return testVariableRepository.save(existingVariable);
    }

    public TestVariable saveVariable(Long testId, Long variableId, Double computedValue, String expression) {
        // 1. Get the original variable (but don't modify it)
        TestVariable originalVariable = testVariableRepository.findById(variableId)
                .orElseThrow(() -> new RuntimeException("Original variable not found"));

        // 2. Create a NEW variable with the same properties but updated content
        TestVariable newVariable = new TestVariable();
        newVariable.setName(originalVariable.getName());
        newVariable.setExpression(expression);
        newVariable.setComputedValue(computedValue);
        newVariable.setMin(originalVariable.getMin());
        newVariable.setMax(originalVariable.getMax());
        newVariable.setUnit(originalVariable.getUnit());

        // 3. Set position to be the next available
        Test test = getTestById(testId);

        // 4. Link to the test and save
        newVariable.setTest(test);
        TestVariable savedVariable = testVariableRepository.save(newVariable);
        // Broadcast the new variable to all subscribers
        broadcastSaveVariable(savedVariable);
        return savedVariable;
    }

    public static void subscribeEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    public static void broadcastUpdate(TestVariable variable) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("variable-updated")
                        .data(variable));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

    public static void broadcastSaveVariable(TestVariable variable) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("variable-saved")
                        .data(variable));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}