package com.cetim.labs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RapportPartiel extends Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private FicheDessai ficheEssai;

    @OneToMany(mappedBy = "rapportPartiel", cascade = CascadeType.ALL)
    private List<RapportEssai> rapportsEssai;

    @ManyToOne
    @JoinColumn(name = "rapport_final_id") // Explicitly define the join column
    private RapportFinal rapportFinal;

    public int getId() {
        return id;
    }

    public FicheDessai getFicheEssai() {
        return ficheEssai;
    }

    public void setFicheEssai(FicheDessai ficheEssai) {
        this.ficheEssai = ficheEssai;
    }

    public List<RapportEssai> getRapportsEssai() {
        return rapportsEssai;
    }

    public void setRapportsEssai(List<RapportEssai> rapportsEssai) {
        this.rapportsEssai = rapportsEssai;
    }

    public RapportFinal getRapportFinal() {
        return rapportFinal;
    }

    public void setRapportFinal(RapportFinal rapportFinal) {
        this.rapportFinal = rapportFinal;
    }

    public void addRapportEssai(RapportEssai rapportEssai) {
        this.rapportsEssai.add(rapportEssai);
        rapportEssai.setRapportPartiel(this);
    }


}
