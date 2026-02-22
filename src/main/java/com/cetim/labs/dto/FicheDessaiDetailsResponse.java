package com.cetim.labs.dto;

import java.util.List;

public class FicheDessaiDetailsResponse {
    private FicheDessaiDTO ficheDessai;
    private List<RapportEssaiFileDTO> rapportsEssai;

    public FicheDessaiDetailsResponse(FicheDessaiDTO ficheDessai, List<RapportEssaiFileDTO> rapportsEssai) {
        this.ficheDessai = ficheDessai;
        this.rapportsEssai = rapportsEssai;
    }

    public FicheDessaiDTO getFicheDessai() {
        return ficheDessai;
    }

    public void setFicheDessai(FicheDessaiDTO ficheDessai) {
        this.ficheDessai = ficheDessai;
    }

    public List<RapportEssaiFileDTO> getRapportsEssai() {
        return rapportsEssai;
    }

    public void setRapportsEssai(List<RapportEssaiFileDTO> rapportsEssai) {
        this.rapportsEssai = rapportsEssai;
    }
}