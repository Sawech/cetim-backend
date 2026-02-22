package com.cetim.labs.dto;

import java.util.List;

public class SousDirectionDTO {
    private Long id;
    private String name;
    private String description;
    List<ServiceDTO> service;

    public SousDirectionDTO(Long id, String name, List<ServiceDTO> service) {
        this.id = id;
        this.name = name;
        this.service = service;
    }

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

    public List<ServiceDTO> getService() {
        return service;
    }

    public void setService(List<ServiceDTO> service) {
        this.service = service;
    }
    
}
