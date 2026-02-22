package com.cetim.labs.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name = "Echantillon")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Echantillon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Echantillonid;

    private String EchantillonCode; 

    @ManyToOne
    private EchantillonTypes echantillonType;
 
    private Date date;

    // Getters and Setters
    public int getId() {
        return Echantillonid;
    }

    public String getEchantillonCode() {
        return EchantillonCode;
    }

    public void setEchantillonCode(String EchantillonCode) {
        this.EchantillonCode = EchantillonCode;
    }

    public EchantillonTypes getEchantillonType() {
        return echantillonType;
    }

    public void setEchantillonType(EchantillonTypes EchantillonType) {
        this.echantillonType = EchantillonType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}