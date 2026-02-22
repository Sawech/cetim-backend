package com.cetim.labs.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(name = "OS",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "OSID")
       })
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServiceOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceOrderID;

    @OneToOne(mappedBy = "OS", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private RapportFinal RE;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<FicheDessai> ficheDessais;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SStatus status = SStatus.NOUVEAU;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public ServiceOrder() {
    }

    public int getServiceOrderID() {
        return serviceOrderID;
    }

    public void setServiceOrderID(int serviceOrderID) {
        this.serviceOrderID = serviceOrderID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SStatus getStatus() {
        return status;
    }

    public void setStatus(SStatus status) {
        this.status = status;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public RapportFinal getRaportFinal() {
        return RE;
    }
    
    public void setRE(RapportFinal RE) {
        this.RE = RE;
    }

    public List<FicheDessai> getFicheDessais() {
        if (ficheDessais == null) {
            ficheDessais = new ArrayList<>();
        }
        return ficheDessais;
    }

    public void setFicheDessais(List<FicheDessai> ficheDessais) {
        this.ficheDessais = ficheDessais;
    }

    public void addFicheDessai(FicheDessai ficheDessai) {
        if (ficheDessais == null) {
            ficheDessais = new ArrayList<>();
        }
        ficheDessais.add(ficheDessai);
        ficheDessai.setServiceOrder(this);
    }

}
