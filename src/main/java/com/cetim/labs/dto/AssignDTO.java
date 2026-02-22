package com.cetim.labs.dto;

public class AssignDTO {

    private String service;
    private int ficheDessaiId;
    private Long userId;
    private Long testId;
    private Long assignNumber;
    private boolean isRead;

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
}
