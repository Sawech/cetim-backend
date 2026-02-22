package com.cetim.labs.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "FicheDessai")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FicheDessai {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    RapportPartiel raportPartiel;

    @ManyToOne
    ServiceOrder serviceOrder;

    @ManyToOne  
    @JoinColumn(name = "order_id")  
    private Order order; 


    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SStatus statu = SStatus.NOUVEAU;

    

    // Getters and Setters
    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getCreationDate() {
        return date;
    }

    public void setCreationDate(Date date) {
        this.date = date;
    }

    public SStatus getStatu() {
        return statu;
    }

    public void setStatu(SStatus statu) {
        this.statu = statu;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public RapportPartiel getRaportPartiel() {
        return raportPartiel;
    }

    public void setRaportPartiel(RapportPartiel raportPartiel) {
        this.raportPartiel = raportPartiel;
    }

}