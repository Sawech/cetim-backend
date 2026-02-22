package com.cetim.labs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "RaportDessai")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RapportEssai extends Rapport {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Test test;

    @ManyToOne
    @JoinColumn(name = "rapport_partiel_id")  // optional but recommended
    private RapportPartiel rapportPartiel;

    public int getId() {
        return id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public RapportPartiel getRapportPartiel(){
        return rapportPartiel;
    }

    public void setRapportPartiel(RapportPartiel rapportPartiel){
        this.rapportPartiel = rapportPartiel;
    }

    public byte[] getFileContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileContent'");
    }
}