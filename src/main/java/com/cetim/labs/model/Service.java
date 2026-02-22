package com.cetim.labs.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sousDirection_id")
    private SousDirection sousDirection;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SousDirection getSousDirection() {
        return sousDirection;
    }

    public void setSousDirection(SousDirection SousDirection) {
        this.sousDirection = SousDirection;
    }
}