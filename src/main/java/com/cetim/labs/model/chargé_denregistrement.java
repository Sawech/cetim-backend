package com.cetim.labs.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("chargé_denregistrement")
public class chargé_denregistrement extends User {

    public chargé_denregistrement() {
        super();
    }

    public chargé_denregistrement(String username, String password) {
        super(username, password);
    }
    
}
