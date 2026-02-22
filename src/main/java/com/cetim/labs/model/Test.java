package com.cetim.labs.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String service;
    private String testCode;
    private String testName;
    private boolean isPrimaryTest; 
    private boolean complexeTest; 

    private String TemplatePath;

    // For SINGLE tests
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    @JsonManagedReference
    private List<TestElement> elements;

    // For SINGLE tests - variable elements
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    @JsonManagedReference
    private List<TestVariable> variables;

    @ElementCollection
    @CollectionTable(
        name = "test_subtests",
        joinColumns = @JoinColumn(name = "parent_test_id")
    )
    @Column(name = "subtest_id")
    private List<Long> subTestIds; 

    @OneToOne(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    RapportEssai rapportEssai;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public List<TestElement> getElements() { return elements; }
    public void setElements(List<TestElement> elements) { this.elements = elements; }
    public String getTestCode() { return testCode; }
    public void setTestCode(String testCode) { this.testCode = testCode; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public boolean isIsPrimaryTest() { return isPrimaryTest; }
    public void setIsPrimaryTest(boolean isPrimaryTest) { this.isPrimaryTest = isPrimaryTest; }
    public List<Long> getSubTestIds() { return subTestIds; }
    public void setSubTestIds(List<Long> subTestIds) { this.subTestIds = subTestIds; }
    public boolean isComplexeTest() { return complexeTest; }
    public void setComplexeTest(boolean complexeTest) { this.complexeTest = complexeTest; }
    public List<TestVariable> getVariables() { return variables; }
    public void setVariables(List<TestVariable> variables) { this.variables = variables; }
    public String getTemplatePath() { return TemplatePath; }
    public void setTemplatePath(String templatePath) { TemplatePath = templatePath; }
    public RapportEssai getRapportEssai() { return rapportEssai; }
    public void setRapportEssai(RapportEssai rapportEssai) { this.rapportEssai = rapportEssai; }
    
}