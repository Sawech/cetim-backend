package com.cetim.labs.config.services;



import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cetim.labs.model.Chef_service;
import com.cetim.labs.model.Directeur;
import com.cetim.labs.model.Operateur;
import com.cetim.labs.model.Sous_directeur;
import com.cetim.labs.model.User;
import com.cetim.labs.model.chargé_denregistrement;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {
    String role;

    if (user instanceof Operateur) {
        role = "Operateur";
    } else if (user instanceof Chef_service) {
        role = "Chef_service";
    } else if (user instanceof Sous_directeur) {
        role = "Sous_directeur";
    } else if (user instanceof Directeur) {
        role = "Directeur";
    } else if (user instanceof chargé_denregistrement) {
        role = "chargé_denregistrement";
    } else {
        throw new IllegalArgumentException("Unknown user subclass: " + user.getClass().getSimpleName());
    }

    GrantedAuthority authority = new SimpleGrantedAuthority(role);
    List<GrantedAuthority> authorities = Collections.singletonList(authority);

    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        authorities
    );
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
