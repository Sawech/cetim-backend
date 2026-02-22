package com.cetim.labs.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "test_variable")
public class TestVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String expression;
    private Double computedValue;
    private Long min;
    private Long max;
    private String unit;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public Double getComputedValue() { return computedValue; }
    public void setComputedValue(Double computedValue) { this.computedValue = computedValue; }
    public Long getMin() { return min; }
    public void setMin(Long min) { this.min = min; }
    public Long getMax() { return max; }
    public void setMax(Long max) { this.max = max; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}