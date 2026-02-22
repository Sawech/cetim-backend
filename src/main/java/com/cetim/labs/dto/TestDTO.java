package com.cetim.labs.dto;

import java.util.List;

public class TestDTO {
    private String service;
    private String testCode;
    private String testName;
    private boolean isPrimaryTest; // Or any other non-reserved name
    private boolean complexeTest; // Add this field
    private List<TestElementDTO> elements;
    private List<TestVariableDTO> variables;  // Add this
    private List<Long> subTestIds;
    private String templatePath; // Add this field if needed
    
    // Getters and setters
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getTestCode() { return testCode; }
    public void setTestCode(String testCode) { this.testCode = testCode; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public boolean isIsPrimaryTest() { return isPrimaryTest; }
    public void setIsPrimaryTest(boolean isPrimaryTest) { this.isPrimaryTest = isPrimaryTest; }
    public List<TestElementDTO> getElements() { return elements; }
    public void setElements(List<TestElementDTO> elements) { this.elements = elements; }
    public List<Long> getSubTestIds() { return subTestIds; }
    public void setSubTestIds(List<Long> subTestIds) { this.subTestIds = subTestIds; }
    public boolean isComplexeTest() { return complexeTest; }
    public void setComplexeTest(boolean complexeTest) { this.complexeTest = complexeTest; }
    public List<TestVariableDTO> getVariables() { return variables; }
    public void setVariables(List<TestVariableDTO> variables) { this.variables = variables; }
    public String getTemplatePath() { return templatePath; }
    public void setTemplatePath(String templatePath) { this.templatePath = templatePath; }
}