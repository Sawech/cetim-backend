package com.cetim.labs.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Assign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    private String service;
    private int ficheDessaiId;
    private Long userId;
    private Long testId;
    private Long assignNumber;
    private boolean isRead;

    @Column(name = "date_exec")
    private LocalDateTime dateExec;

    @PrePersist
    protected void onCreate() {
        dateExec = LocalDateTime.now();
    }
    
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public int getFicheDessaiId() {
        return ficheDessaiId;
    }
    public void setFicheDessaiId(int ficheDessaiId) {
        this.ficheDessaiId = ficheDessaiId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getTestId() {
        return testId;
    }
    public void setTestId(Long testId) {
        this.testId = testId;
    }
    public Long getAssignNumber() {
        return assignNumber;
    }
    public void setAssignNumber(Long assignNumber) {
        this.assignNumber = assignNumber;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    public LocalDateTime getDateExec() {
        return dateExec;
    }
    public void setDateExec(LocalDateTime dateExec) {
        this.dateExec = dateExec;
    }

}