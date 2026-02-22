package com.cetim.labs.dto;

public class RapportEssaiFileDTO {
    private int id;
    private String downloadUrl;

    public RapportEssaiFileDTO(int id, String downloadUrl) {
        this.id = id;
        this.downloadUrl = downloadUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }   

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    


}
