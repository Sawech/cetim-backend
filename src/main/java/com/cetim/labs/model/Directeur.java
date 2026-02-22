package com.cetim.labs.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Directeur")
public class Directeur extends User {

    public Directeur() {
        super();
    }

    public Directeur(String username, String password) {
        super(username, password);
    }
}
