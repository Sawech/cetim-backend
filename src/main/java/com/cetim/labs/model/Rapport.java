package com.cetim.labs.model;

import java.util.Date;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Rapport {
    

    private SStatus status;

    private Date dateCreation;
    private Date dateValidation;
  

    public SStatus getStatus() {
        return status;
    }

    public void setStatus(SStatus status) {
        this.status = status;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

}
