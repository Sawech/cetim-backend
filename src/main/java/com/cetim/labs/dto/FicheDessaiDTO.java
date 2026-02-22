package com.cetim.labs.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.Order;

public class FicheDessaiDTO {
    private FicheDessai ficheDessai;

    private int id;
    private int serviceOrderID;
    private int raportFinalRE;
    private int raportPartielID;
    private Order order;
    private Date osDate;

    public FicheDessaiDTO(FicheDessai ficheDessai, int raportFinalRE, int serviceOrderID, Date osDate) {
        this.ficheDessai = ficheDessai;
        this.raportFinalRE = raportFinalRE;
        this.serviceOrderID = serviceOrderID;
        this.osDate = osDate;
    }

    public FicheDessaiDTO(int id, int raportFinalRE, int serviceOrderID, Date osDate, Order order) {
        this.id = id;
        this.raportFinalRE = raportFinalRE;
        this.serviceOrderID = serviceOrderID;
        this.osDate = osDate;
        this.order = order;
    }

    public FicheDessaiDTO(int id, int raportFinalRE,int raportPartielID, int serviceOrderID, Date osDate, Order order) {
        this.id = id;
        this.raportFinalRE = raportFinalRE;
        this.raportPartielID = raportPartielID;
        this.serviceOrderID = serviceOrderID;
        this.osDate = osDate;
        this.order = order;
    }
    

    
    public FicheDessai getFicheDessai() {
        return ficheDessai;
    }

    public void setFicheDessai(FicheDessai ficheDessai) {
        this.ficheDessai = ficheDessai;
    }

    public int getRaportFinalRE() {
        return raportFinalRE;
    }

    public void setRaportFinalRE(int raportFinalRE) {
        this.raportFinalRE = raportFinalRE;
    }

    public int getServiceOrderID() {
        return serviceOrderID;
    }

    public void setServiceOrderID(int serviceOrderID) {
        this.serviceOrderID = serviceOrderID;
    }

    public Date getOsDate() {
        return osDate;
    }

    public void setOsDate(Date osDate) {
        this.osDate = osDate;
    }   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRaportPartielID() {
        return raportPartielID;
    }

    public void setRaportPartielID(int raportPartielID) {
        this.raportPartielID = raportPartielID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}