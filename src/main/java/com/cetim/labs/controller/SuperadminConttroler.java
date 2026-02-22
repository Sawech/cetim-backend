package com.cetim.labs.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cetim.labs.dto.request.SignupRequest;
import com.cetim.labs.dto.response.MessageResponse;
import com.cetim.labs.model.Chef_service;
import com.cetim.labs.model.Directeur;
import com.cetim.labs.model.Operateur;
import com.cetim.labs.model.Sous_directeur;
import com.cetim.labs.model.User;
import com.cetim.labs.model.chargé_denregistrement;
import com.cetim.labs.repository.UserRepository;
import com.cetim.labs.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/superadmin")
public class SuperadminConttroler {
	
	@Autowired
	  PasswordEncoder encoder;
	
	@Autowired
	  UserRepository userRepository;
	
	@Autowired
    private UserService userService;

	// ...existing code...
	@PostMapping("/newuser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		User user ;

		String Role = signUpRequest.getRole();

		if (Role.equalsIgnoreCase("Directeur")) {
			user = new Directeur(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
		} else if (Role.equalsIgnoreCase("Sous_directeur")) {
			user = new Sous_directeur(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
		} else if (Role.equalsIgnoreCase("Chef_service")) {
			user = new Chef_service(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
		} else if (Role.equalsIgnoreCase("Operateur")) {
			user = new Operateur(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
		} else if (Role.equalsIgnoreCase("chargé_denregistrement")) {
			user = new chargé_denregistrement(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Role is not valid!"));
		}

		// Set email only if provided
		if (signUpRequest.getEmail() != null && !signUpRequest.getEmail().isEmpty()) {
			user.setEmail(signUpRequest.getEmail());
		}

		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setphonenumber(signUpRequest.getPhonenumber());

		System.out.println("Before setting sousDirection: " + signUpRequest.getSousDirection());
		user.setSousDirection(signUpRequest.getSousDirection());
		System.out.println("After setting sousDirection: " + user.getSousDirection());

		System.out.println("Before setting service: " + signUpRequest.getService());
		user.setService(signUpRequest.getService());
		System.out.println("After setting service: " + user.getService());

		user.setNotes(signUpRequest.getNotes());

		System.out.println("User before save: " + user);

		userRepository.save(user);

		System.out.println("User saved successfully!");

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	// ...existing code...
		
	@GetMapping("/getusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
	
	@GetMapping("/getusers/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/updateusers/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/deleteusers/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error deleting user: " + e.getMessage()));
        }
    }
}
