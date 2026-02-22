package com.cetim.labs.dto;

public class RapportDessaiRequestDTO {
    private long testId;
    private int ficheDessaiId;
    
    // Getters and setters
    public long getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }
    public int getFicheDessaiId() { return ficheDessaiId; }
    public void setFicheDessaiId(int ficheDessaiId) { this.ficheDessaiId = ficheDessaiId; }
}