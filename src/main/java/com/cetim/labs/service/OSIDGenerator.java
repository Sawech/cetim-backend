package com.cetim.labs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.cetim.labs.model.ServiceOrder;
import com.cetim.labs.repository.OSRepository;

@Service
public class OSIDGenerator {
    
    @Autowired
    private OSRepository osRepository;
    
    @Transactional
    public String generateOSID() {
        // Get all OS IDs for the current year
        List<ServiceOrder> allOS = osRepository.findAll();
        
        // Find the highest number used for the current year
        int maxNumber = 0;
        
        for (ServiceOrder os : allOS) {
            int id = os.getServiceOrderID();
            if (id != 0) {
                try {
                    // Extract the number part (last 4 digits)
                    int numberStr = id;
                    int number = numberStr;
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    // Skip invalid IDs
                }
            }
        }
        
        // Increment the number
        int nextNumber = maxNumber + 1;
        if (nextNumber > 9999) {
            throw new RuntimeException("OS counter exceeded maximum value (9999) for year ");
        }
        
        // Format number with leading zeros
        String number = String.format("%04d", nextNumber);
        return number;
    }
} 