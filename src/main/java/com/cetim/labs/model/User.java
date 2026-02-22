package com.cetim.labs.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username")
       })
public class User {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	
	  @NotBlank
	  @Size(max = 20)
	  private String username;
	
	  @NotBlank
	  @Size(max = 120)
	  private String password;
	  
	  private String firstName;
	  private String lastName;
	  private String email;
	  private String phonenumber;
	  private String SousDirection;
	  private String service;
	  private String notes;
	  
	  @CreationTimestamp
	  private LocalDateTime createdAt;
	  
	  @UpdateTimestamp
	  private LocalDateTime updatedAt;
	  
	
	  public User() {
	  }
	
	  public User(String username, String password) {
	    this.username = username;
	    this.password = password;
	  }
	
	  public Long getId() {
	    return id;
	  }
	
	  public void setId(Long id) {
	    this.id = id;
	  }
	
	  public String getUsername() {
	    return username;
	  }
	
	  public void setUsername(String username) {
	    this.username = username;
	  }
	
	  public String getEmail() {
	    return email;
	  }
	
	  public void setEmail(String email) {
	    this.email = email;
	  }
	
	  public String getPassword() {
	    return password;
	  }
	
	  public void setPassword(String password) {
	    this.password = password;
	  }
	  
	  public String getFirstName() {
	    return firstName;
	  }
	
	  public void setFirstName(String firstName) {
	    this.firstName = firstName;
	  }
	  
	  public String getLastName() {
	    return lastName;
	  }
	
	  public void setLastName(String lastName) {
	    this.lastName = lastName;
	  }
	  
	  public String getphonenumber() {
	    return phonenumber;
	  }
	
	  public void setphonenumber(String phonenumber) {
	    this.phonenumber = phonenumber;
	  }

	public String getSousDirection() {
		return SousDirection;
	}
	
	public void setSousDirection(String SousDirection) {
		this.SousDirection = SousDirection;
	}

	public String getService() {
		return service;
	}

	public void setService(String services) {
		this.service = services;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}