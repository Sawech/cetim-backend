package com.cetim.labs.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Table(name = "RapportFinal",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "RE")
       })
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RapportFinal extends Rapport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "service_orderid")
    private ServiceOrder OS;

    @OneToMany(mappedBy = "rapportFinal", cascade = CascadeType.ALL)
    private List<RapportPartiel> rapportsPartiels;

    public int getId() {
        return id;
    }

    public ServiceOrder getOS() {
        return OS;
    }

    public void setOS(ServiceOrder oS) {
        OS = oS;
    }

    public List<RapportPartiel> getRapportsPartiels() {
        if (rapportsPartiels == null) {
            rapportsPartiels = new ArrayList<>();
        }
        return rapportsPartiels;
    }

    public void setRapportsPartiels(List<RapportPartiel> rapportsPartiels) {
        this.rapportsPartiels = rapportsPartiels;
    }

    public void addRapportPartiel(RapportPartiel rapportPartiel) {
        if (rapportsPartiels == null) {
            rapportsPartiels = new ArrayList<>();
        }
        rapportsPartiels.add(rapportPartiel);
        rapportPartiel.setRapportFinal(this);
    }
}