package com.cetim.labs.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Sous_directeur")
public class Sous_directeur extends User {

    public Sous_directeur() {
        super();
    }

    public Sous_directeur(String username, String password) {
        super(username, password);
    }
}
