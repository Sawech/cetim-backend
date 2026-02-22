package com.cetim.labs.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cetim.labs.config.jwt.JwtUtils;
import com.cetim.labs.config.services.UserDetailsImpl;
import com.cetim.labs.dto.request.LoginRequest;
import com.cetim.labs.dto.response.MessageResponse;
import com.cetim.labs.dto.response.UserInfoResponse;
import com.cetim.labs.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepo;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    logger.info("Authentication attempt for user: {}", loginRequest.getUsername());
    
    try {
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      logger.info("User {} successfully authenticated", userDetails.getUsername());

      ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
      logger.debug("JWT cookie generated for user: {}", userDetails.getUsername());

      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());
      
      logger.info("User {} roles: {}", userDetails.getUsername(), roles);

      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
          .body(new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles , userRepo.findByUsername(userDetails.getUsername()).get().getSousDirection(), userRepo.findByUsername(userDetails.getUsername()).get().getService()));
    } catch (Exception e) {
      logger.error("Authentication failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
      throw e;
    }
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth != null && auth.getPrincipal() instanceof UserDetailsImpl ? 
        ((UserDetailsImpl) auth.getPrincipal()).getUsername() : "unknown";
    
    logger.info("Logout request received for user: {}", username);
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    logger.info("User {} successfully logged out", username);
    
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}