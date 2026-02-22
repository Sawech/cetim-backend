package com.cetim.labs.dto;

import com.cetim.labs.model.FicheDessai;

public class RaportDessaiDTO {

    private FicheDessai ficheDessai;
    private String osDate;
    private String raportFinalRE;
    private String serviceOrderID;



    public RaportDessaiDTO(FicheDessai ficheDessai) {
        this.ficheDessai = ficheDessai;
    }

    public FicheDessai getFicheDessai() {
        return ficheDessai;
    }
    public void setFicheDessai(FicheDessai ficheDessai) {
        this.ficheDessai = ficheDessai;
    }

    public String getOsDate() {
        return osDate;
    }

    public void setOsDate(String osDate) {
        this.osDate = osDate;
    }

    public String getRaportFinalRE() {
        return raportFinalRE;
    }

    public void setRaportFinalRE(String raportFinalRE) {
        this.raportFinalRE = raportFinalRE;
    }

    public String getServiceOrderID() {
        return serviceOrderID;
    }

    public void setServiceOrderID(String serviceOrderID) {
        this.serviceOrderID = serviceOrderID;
    }

    
}
