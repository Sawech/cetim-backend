package com.cetim.labs.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Chef_service")
public class Chef_service extends User {

    public Chef_service() {
        super();
    }

    public Chef_service(String username, String password) {
        super(username, password);
    }
    
}
