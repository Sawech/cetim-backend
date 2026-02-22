package com.cetim.labs.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Operateur")
public class Operateur extends User {

    public Operateur() {
        super();
    }

    public Operateur(String username, String password) {
        super(username, password);
    }
    
}
