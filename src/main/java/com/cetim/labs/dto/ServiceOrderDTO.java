package com.cetim.labs.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cetim.labs.model.SStatus;

public class ServiceOrderDTO {
    private Date date;
    private int OSID; 
    private Integer raportFinalRE;
    private List<OrderDTO> orders = new ArrayList<>();
    private SStatus status;

    public ServiceOrderDTO() {}

    public Integer getRaportFinalRE() {return raportFinalRE;}
    public void setRaportFinalRE(Integer raportFinalRE) {this.raportFinalRE = raportFinalRE;}

    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}

    public int getOSID() {return OSID;}
    public void setOSID(int OSID) {this.OSID = OSID;}

    public List<OrderDTO> getOrders() {return orders;}
    public void setOrders(List<OrderDTO> orders) {this.orders = orders;}
    public void addOrder(OrderDTO order) {this.orders.add(order);}

    public SStatus getStatus() {return status;}
    public void setStatus(SStatus status) {this.status = status;}
}

