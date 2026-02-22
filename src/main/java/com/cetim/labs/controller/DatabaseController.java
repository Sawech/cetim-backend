package com.cetim.labs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cetim.labs.model.DatabaseConfig;
import com.cetim.labs.service.DatabaseService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/check")
    public Map<String, String> checkDatabase() {
    	Map<String, String> response = new HashMap<>();
        if( databaseService.checkconfig()) {
        	response.put("message", "Database connected.");
        	response.put("status", "connected");
        } else {
        	response.put("message", "Database n est pas configurer.");
        	response.put("status", "error");
        };
		return response;
    }
    
    @PostMapping("/setup")
    public Map<String, String> configureDatabase(@RequestBody DatabaseConfig config) {
       
        Map<String, String> testResult = databaseService.checkConnection(config);
        
        if ("success".equals(testResult.get("status"))) {
            databaseService.saveConfiguration(config);
            testResult.put("message", "Database configuration saved and connection verified.");
            testResult.put("status", "connected");
        }

        return testResult;
    }
}
